package by.bsuir.hockeyshop.dao.impl;

import by.bsuir.hockeyshop.dao.OrderDao;
import by.bsuir.hockeyshop.entity.Item;
import by.bsuir.hockeyshop.entity.Order;
import by.bsuir.hockeyshop.dao.DaoException;
import by.bsuir.hockeyshop.entity.User;
import by.bsuir.hockeyshop.pool.ConnectionPool;
import by.bsuir.hockeyshop.managers.ConfigurationManager;

import java.io.File;
import java.sql.*;
import java.util.*;
/**
 * {@inheritDoc}
 *
 * This implementation is based on JDBC and MySQL. It is also a singleton. Connecetions are taken and returned to the pool
 * in each method.
 */
public class OrderDaoImpl implements OrderDao {
    static final String SELECT_ALL_ORDERS = "SELECT order.user_id, login, `order`.order_id,  " +
            "creation_date, payment_date, SUM(item.price*order_item.quantity) AS totalSum, SUM(quantity) AS totalCount " +
            "FROM `order` " +
            "JOIN order_item ON `order`.order_id = order_item.order_id " +
            "JOIN item ON order_item.item_id = item.item_id " +
            "JOIN user ON user.user_id = `order`.user_id " +
            "WHERE creation_date IS NOT NULL " +
            "GROUP BY `order`.order_id " +
            "ORDER BY creation_date DESC " +
            "LIMIT ? OFFSET ?";
    static final String SELECT_ALL_PAID_ORDERS = "SELECT order.user_id, login, `order`.order_id,  " +
            "creation_date, payment_date, SUM(item.price*order_item.quantity) AS totalSum, SUM(quantity) AS totalCount " +
            "FROM `order` " +
            "JOIN order_item ON `order`.order_id = order_item.order_id " +
            "JOIN item on order_item.item_id = item.item_id " +
            "JOIN user ON user.user_id = `order`.user_id " +
            "WHERE creation_date IS NOT NULL AND payment_date IS NOT NULL " +
            "GROUP BY `order`.order_id " +
            "ORDER BY creation_date DESC " +
            "LIMIT ? OFFSET ?";
    static final String SELECT_ALL_UNPAID_ORDERS= "SELECT order.user_id, login, `order`.order_id, " +
            "creation_date, payment_date, SUM(item.price*order_item.quantity) AS totalSum, SUM(quantity) AS totalCount " +
            "FROM `order` " +
            "JOIN order_item ON `order`.order_id = order_item.order_id " +
            "JOIN item on order_item.item_id = item.item_id " +
            "JOIN user ON user.user_id = `order`.user_id " +
            "WHERE creation_date IS NOT NULL AND payment_date IS NULL " +
            "GROUP BY `order`.order_id " +
            "ORDER BY creation_date DESC " +
            "LIMIT ? OFFSET ?";
    static final String SELECT_ALL_ORDERS_BY_USER_ID = "SELECT `order`.order_id,  " +
            "creation_date, payment_date, SUM(item.price*order_item.quantity) AS totalSum, SUM(quantity) AS totalCount " +
            "FROM `order` " +
            "JOIN order_item ON `order`.order_id = order_item.order_id " +
            "JOIN item on order_item.item_id = item.item_id " +
            "WHERE user_id=? " +
            "GROUP BY `order`.order_id " +
            "ORDER BY creation_date DESC " +
            "LIMIT ? OFFSET ?";
    static final String GET_UNPAID_ORDERS_BY_USER_ID = "SELECT `order`.order_id,  " +
            "creation_date, payment_date, SUM(item.price*order_item.quantity) AS totalSum, SUM(quantity) AS totalCount " +
            "FROM `order` " +
            "JOIN order_item ON `order`.order_id = order_item.order_id " +
            "JOIN item on order_item.item_id = item.item_id " +
            "WHERE user_id=? AND payment_date IS NULL " +
            "GROUP BY `order`.order_id " +
            "ORDER BY creation_date DESC " +
            "LIMIT ? OFFSET ?";
    static final String GET_PAID_ORDERS_BY_USER_ID = "SELECT `order`.order_id,  " +
            "creation_date, payment_date, SUM(item.price*order_item.quantity) AS totalSum, SUM(quantity) AS totalCount " +
            "FROM `order` " +
            "JOIN order_item ON `order`.order_id = order_item.order_id " +
            "JOIN item on order_item.item_id = item.item_id " +
            "WHERE user_id=? AND payment_date IS NOT NULL " +
            "GROUP BY `order`.order_id " +
            "ORDER BY creation_date DESC " +
            "LIMIT ? OFFSET ?";
    static final String INSERT_NEW_ORDER = "INSERT INTO `order` (user_id) VALUES(?)";
    static final String ADD_ITEM_TO_ORDER = "INSERT INTO order_item (item_id, order_id, quantity) " +
            "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE quantity = quantity + ?";
    static final String REMOVE_ITEM_FROM_ORDER = "DELETE FROM order_item WHERE order_id=? AND item_id=?";
    static final String UPDATE_ORDER_PAYMENT = "UPDATE `order` SET payment_date=now() WHERE order_id=?";
    static final String DELETE_ORDER = "DELETE FROM `order` WHERE order_id=? AND payment_date IS NULL";
    static final String DELETE_LATE_ORDER = "DELETE FROM `order` WHERE order_id=? " +
            "AND creation_date < DATE_SUB(now(), INTERVAL 3 DAY) AND payment_date IS NULL";
    static final String UPDATE_ORDER_STATUS = "UPDATE `order` SET creation_date=now() WHERE order_id=?";
    static final String SELECT_CURRENT_ORDER= "SELECT `order`.order_id,  creation_date, payment_date, " +
            "SUM(item.price*order_item.quantity) AS totalSum, SUM(quantity) AS totalCount " +
            "FROM `order` JOIN order_item ON `order`.order_id=order_item.order_id " +
            "JOIN item on order_item.item_id = item.item_id " +
            "WHERE user_id=? AND creation_date IS NULL";
    static final String SELECT_ORDER_BY_ID= "SELECT  creation_date, payment_date, SUM(item.price*order_item.quantity) AS totalSum, SUM(quantity) AS totalCount " +
            "FROM `order` JOIN order_item ON `order`.order_id=order_item.order_id " +
            "JOIN item on order_item.item_id = item.item_id " +
            "WHERE `order`.order_id=?";
    static final String SELECT_CURRENT_ORDER_ID = "SELECT order_id FROM `order` WHERE user_id=? AND creation_date IS NULL";
    static final String SELECT_DISTINCT_ITEMS_COUNT_IN_ORDER = "SELECT COUNT(item_id) AS `count` FROM order_item WHERE order_id=?";
    static final String GET_ITEMS_BY_ORDER_ID = "SELECT item.item_id, `name`, size, color, price, quantity, image_path " +
            "FROM order_item " +
            "JOIN item ON order_item.item_id = item.item_id "+
            "WHERE order_id = ?";
    static final String SELECT_ORDER_COUNT_FOR_USER = "SELECT COUNT(order_id) AS `count` FROM user " +
            "LEFT JOIN `order` ON user.user_id = `order`.user_id " +
            "WHERE user.user_id=? AND creation_date IS NOT NULL";
    static final String SELECT_UNPAID_ORDER_COUNT_FOR_USER = "SELECT COUNT(order_id) AS `count` FROM user " +
            "LEFT JOIN `order` ON user.user_id = `order`.user_id " +
            "WHERE user.user_id=? AND creation_date IS NOT NULL AND payment_date IS NULL";
    static final String SELECT_PAID_ORDER_COUNT_FOR_USER = "SELECT COUNT(order_id) AS `count` FROM user " +
            "LEFT JOIN `order` ON user.user_id = `order`.user_id " +
            "WHERE user.user_id=? AND creation_date IS NOT NULL AND payment_date IS NOT NULL";
    static final String SELECT_LATE_ORDER_COUNT_FOR_USER = "SELECT COUNT(order_id) AS `count` FROM user " +
            "LEFT JOIN `order` ON user.user_id = `order`.user_id " +
            "WHERE user.user_id=? AND creation_date < DATE_SUB(now(), INTERVAL 3 DAY) AND payment_date IS NULL";
    static final String SELECT_ORDER_OWNER = "SELECT user_id FROM `order` WHERE order_id=?";
    private static OrderDao instance = new OrderDaoImpl();

    private OrderDaoImpl() {}

    public static OrderDao getInstance() {
        return instance;
    }

    @Override
    public List<Order> selectOrdersByUserId(long id, int offset, int limit) throws DaoException{
        return getOrdersByQueryAndUserId(SELECT_ALL_ORDERS_BY_USER_ID, id, offset, limit);
    }

    @Override
    public List<Order> selectPaidOrdersByUserId(long id, int offset, int limit) throws DaoException {
        return getOrdersByQueryAndUserId(GET_PAID_ORDERS_BY_USER_ID, id, offset, limit);
    }

    @Override
    public List<Order> selectUnpaidOrdersByUserId(long id, int offset, int limit) throws DaoException {
        return getOrdersByQueryAndUserId(GET_UNPAID_ORDERS_BY_USER_ID, id, offset, limit);
    }

    @Override
    public List<Order> selectAllOrders(int offset, int limit) throws DaoException {
        return selectAllOrdersByQuery(offset, limit, SELECT_ALL_ORDERS);
    }

    @Override
    public List<Order> selectAllUnpaidOrders(int offset, int limit) throws DaoException {
        return selectAllOrdersByQuery(offset, limit, SELECT_ALL_UNPAID_ORDERS);
    }

    @Override
    public List<Order> selectAllPaidOrders(int offset, int limit) throws DaoException {
        return selectAllOrdersByQuery(offset, limit, SELECT_ALL_PAID_ORDERS);
    }

    public List<Order> selectAllOrdersByQuery(int offset, int limit, String query) throws DaoException {
        List<Order> orders = new ArrayList<>();
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(query)) {
            st.setInt(1, limit);
            st.setInt(2, offset);
            ResultSet resultSet = st.executeQuery();
            while(resultSet.next()) {
                Order order = new Order();
                User user = new User(resultSet.getLong("user_id"));
                user.setLogin(resultSet.getString("login"));
                order.setUser(user);
                order.setId(resultSet.getLong("order_id"));
                GregorianCalendar gc = new GregorianCalendar();
                Timestamp timestamp = resultSet.getTimestamp("creation_date");
                if (timestamp != null) {
                    gc.setTime(timestamp);
                    order.setCreationDateTime(gc);
                    gc = new GregorianCalendar();
                    timestamp = resultSet.getTimestamp("payment_date");
                    if (timestamp != null) {
                        gc.setTime(timestamp);
                        order.setPaymentDateTime(gc);
                    }
                }
                order.setPaymentSum(resultSet.getInt("totalSum"));
                order.setCountOfItems(resultSet.getInt("totalCount"));
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return orders;
    }

    private List<Order> getOrdersByQueryAndUserId(String query, long id, int offset, int limit) throws DaoException {
        List<Order> orders = new ArrayList<>();
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(query)) {
            st.setLong(1, id);
            st.setInt(2, limit);
            st.setInt(3, offset);
            ResultSet resultSet = st.executeQuery();
            while(resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt("order_id"));
                GregorianCalendar gc = new GregorianCalendar();
                Timestamp timestamp = resultSet.getTimestamp("creation_date");
                if (timestamp != null) {
                    gc.setTime(timestamp);
                    order.setCreationDateTime(gc);
                    gc = new GregorianCalendar();
                    timestamp = resultSet.getTimestamp("payment_date");
                    if (timestamp != null) {
                        gc.setTime(timestamp);
                        order.setPaymentDateTime(gc);
                    }
                }
                order.setPaymentSum(resultSet.getInt("totalSum"));
                order.setCountOfItems(resultSet.getInt("totalCount"));
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return orders;
    }

    @Override
    public Order selectCurrentOrder(long userId) throws DaoException {
        Order order = null;
        try (Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(SELECT_CURRENT_ORDER)) {
            st.setLong(1, userId);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                order = new Order();
                order.setId(resultSet.getLong("order_id"));
                order.setCountOfItems(resultSet.getInt("totalCount"));
                order.setPaymentSum(resultSet.getInt("totalSum"));
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return order;
    }

    public boolean deleteLateOrder(long orderId) throws DaoException {
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(DELETE_LATE_ORDER)) {
            st.setLong(1, orderId);
            if (st.executeUpdate() == 0) {
                return false;
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return true;
    }

    @Override
    public Order selectOrder(long orderId) throws DaoException {
        Order order = null;
        try (Connection cn = ConnectionPool.getInstance().takeConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_ORDER_BY_ID)) {
            st.setLong(1, orderId);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                order = new Order();
                order.setId(orderId);
                order.setCountOfItems(resultSet.getInt("totalCount"));
                order.setPaymentSum(resultSet.getInt("totalSum"));
                GregorianCalendar gc = new GregorianCalendar();
                Timestamp timestamp = resultSet.getTimestamp("creation_date");
                if (timestamp != null) {
                    gc.setTime(timestamp);
                    order.setCreationDateTime(gc);
                    gc = new GregorianCalendar();
                    timestamp = resultSet.getTimestamp("payment_date");
                    if (timestamp != null) {
                        gc.setTime(timestamp);
                        order.setPaymentDateTime(gc);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return order;
    }



    @Override
    public Long selectCurrentOrderId(long userId) throws DaoException {
        Long orderId = null;
        try (Connection cn = ConnectionPool.getInstance().takeConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_CURRENT_ORDER_ID)) {
            st.setLong(1, userId);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                orderId = resultSet.getLong("order_id");
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return orderId;
    }

    @Override
    public Long insertNewOrder(long userId) throws DaoException {
        Long result = null;
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(INSERT_NEW_ORDER, PreparedStatement.RETURN_GENERATED_KEYS)) {
            st.setLong(1, userId);
            st.executeUpdate();
            ResultSet resultSet = st.getGeneratedKeys();
            if (resultSet.next()){
                result = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return result;
    }

    @Override
    public boolean addItemsToOrder(long itemId, int count, long orderId) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(ADD_ITEM_TO_ORDER)) {
                st.setLong(1, itemId);
                st.setLong(2, orderId);
                st.setInt(3, count);
                st.setInt(4, count);
            if (st.executeUpdate() == 0) {
                return false;
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return true;
    }

    @Override
    public boolean removeItemsFromOrder(long itemId, long orderId) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().takeConnection();
             PreparedStatement st = cn.prepareStatement(REMOVE_ITEM_FROM_ORDER)) {
            st.setLong(1, orderId);
            st.setLong(2, itemId);
            if (st.executeUpdate() == 0) {
                return false;
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return true;
    }

    @Override
    public boolean updateOrderPayment(long orderId) throws DaoException{
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(UPDATE_ORDER_PAYMENT)) {
            st.setLong(1, orderId);
            if (st.executeUpdate() == 0) {
                return false;
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return true;
    }

    @Override
    public boolean deleteOrder(long id) throws DaoException{
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(DELETE_ORDER)) {
            st.setLong(1, id);
            if (st.executeUpdate() == 0) {
                return false;
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return true;
    }

    @Override
    public boolean updateOrderStatusToSubmitted(long id) throws DaoException {
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(UPDATE_ORDER_STATUS)) {
            st.setLong(1, id);
            if (st.executeUpdate() == 0) {
                return false;
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return true;
    }

    @Override
    public int selectSubmittedOrdersCountByUser(long userId) throws DaoException{
        int count = 0;
        try (Connection cn = ConnectionPool.getInstance().takeConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_ORDER_COUNT_FOR_USER)) {
            st.setLong(1, userId);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return count;
    }

    @Override
    public int selectLateOrdersCountByUser(long userId) throws DaoException{
        int count = 0;
        try (Connection cn = ConnectionPool.getInstance().takeConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_LATE_ORDER_COUNT_FOR_USER)) {
            st.setLong(1, userId);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return count;
    }


    @Override
    public int selectPaidOrdersCountByUser(long userId) throws DaoException{
        int count = 0;
        try (Connection cn = ConnectionPool.getInstance().takeConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_PAID_ORDER_COUNT_FOR_USER)) {
            st.setLong(1, userId);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return count;
    }

    @Override
    public int selectUnpaidOrdersCountByUser(long userId) throws DaoException{
        int count = 0;
        try (Connection cn = ConnectionPool.getInstance().takeConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_UNPAID_ORDER_COUNT_FOR_USER)) {
            st.setLong(1, userId);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return count;
    }

    @Override
    public int selectItemCountInOrder(long orderId) throws DaoException {
        int count = 0;
        try (Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(SELECT_DISTINCT_ITEMS_COUNT_IN_ORDER)) {
            st.setLong(1, orderId);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return count;
    }

    @Override
    public Map<Item, Integer> selectItemsByOrderId(long id) throws DaoException {
        Map<Item, Integer> items = new HashMap<>();
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(GET_ITEMS_BY_ORDER_ID)) {
            st.setLong(1, id);
            ResultSet resultSet = st.executeQuery();
            while(resultSet.next()) {
                Item item = new Item();
                item.setId(Long.parseLong(resultSet.getString("item_id")));
                item.setName(resultSet.getString("name"));
                item.setSize(resultSet.getString("size"));
                item.setColor(resultSet.getString("color"));
                item.setPrice(resultSet.getInt("price"));
                item.setImagePath(ConfigurationManager.getProperty("path.items")
                        + File.separator+resultSet.getString("image_path"));
                int quantity = resultSet.getInt("quantity");
                if (items.containsKey(item)) {
                    items.put(item, items.get(item) + quantity);
                } else {
                    items.put(item, quantity);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return items;
    }

    @Override
    public Long selectOrderOwnerId(long orderId) throws DaoException {
        Long id = null;
        try (Connection cn = ConnectionPool.getInstance().takeConnection();
             PreparedStatement st = cn.prepareStatement(SELECT_ORDER_OWNER)) {
            st.setLong(1, orderId);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getLong("user_id");
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return id;
    }
}
