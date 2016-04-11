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
    private static final String SELECT_ALL_ORDERS = "SELECT order.user_id, login, `order`.order_id,  " +
            "creation_date, payment_date, SUM(item_price.price*order_item.quantity) AS totalSum, SUM(quantity) AS totalCount " +
            "FROM `order` " +
            "JOIN order_item ON `order`.order_id = order_item.order_id " +
            "JOIN item ON order_item.item_id = item.item_id " +
            "JOIN user ON user.user_id = `order`.user_id " +
            "JOIN item_price ON item.item_id = item_price.item_id AND order_item.add_date BETWEEN item_price.starts AND item_price.ends " +
            "WHERE creation_date IS NOT NULL " +
            "GROUP BY `order`.order_id " +
            "ORDER BY creation_date DESC ";
    private static final String SELECT_ALL_PAID_ORDERS = "SELECT order.user_id, login, `order`.order_id,  " +
            "creation_date, payment_date, SUM(item_price.price*order_item.quantity) AS totalSum, SUM(quantity) AS totalCount " +
            "FROM `order` " +
            "JOIN order_item ON `order`.order_id = order_item.order_id " +
            "JOIN item on order_item.item_id = item.item_id " +
            "JOIN item_price ON item.item_id = item_price.item_id AND order_item.add_date BETWEEN item_price.starts AND item_price.ends " +
            "JOIN user ON user.user_id = `order`.user_id " +
            "WHERE creation_date IS NOT NULL AND payment_date IS NOT NULL " +
            "GROUP BY `order`.order_id " +
            "ORDER BY creation_date DESC ";
    private static final String SELECT_ALL_UNPAID_ORDERS= "SELECT order.user_id, login, `order`.order_id, " +
            "creation_date, payment_date, SUM(item_price.price*order_item.quantity) AS totalSum, SUM(quantity) AS totalCount " +
            "FROM `order` " +
            "JOIN order_item ON `order`.order_id = order_item.order_id " +
            "JOIN item on order_item.item_id = item.item_id " +
            "JOIN item_price ON item.item_id = item_price.item_id AND order_item.add_date BETWEEN item_price.starts AND item_price.ends " +
            "JOIN user ON user.user_id = `order`.user_id " +
            "WHERE creation_date IS NOT NULL AND payment_date IS NULL " +
            "GROUP BY `order`.order_id " +
            "ORDER BY creation_date DESC ";
    private static final String SELECT_ALL_ORDERS_BY_USER_ID = "SELECT `order`.order_id,  " +
            "creation_date, payment_date, SUM(item_price.price*order_item.quantity) AS totalSum, " +
            "SUM(quantity) AS totalCount, `user`.login " +
            "FROM `order` " +
            "JOIN order_item ON `order`.order_id = order_item.order_id " +
            "JOIN item ON order_item.item_id = item.item_id " +
            "JOIN item_price ON item.item_id = item_price.item_id AND order_item.add_date BETWEEN item_price.starts AND item_price.ends " +
            "JOIN user ON `order`.user_id = `user`.user_id " +
            "WHERE `order`.user_id=? AND creation_date IS NOT NULL " +
            "GROUP BY `order`.order_id " +
            "ORDER BY creation_date DESC ";
    private static final String SELECT_UNPAID_ORDERS_BY_USER_ID = "SELECT `order`.order_id,  " +
            "creation_date, payment_date, SUM(item_price.price*order_item.quantity) AS totalSum, " +
            "SUM(quantity) AS totalCount, `user`.login  " +
            "FROM `order` " +
            "JOIN order_item ON `order`.order_id = order_item.order_id " +
            "JOIN item on order_item.item_id = item.item_id " +
            "JOIN item_price ON item.item_id = item_price.item_id AND order_item.add_date BETWEEN item_price.starts AND item_price.ends " +
            "JOIN user ON `order`.user_id = `user`.user_id " +
            "WHERE `order`.user_id=? AND payment_date IS NULL AND creation_date IS NOT NULL " +
            "GROUP BY `order`.order_id " +
            "ORDER BY creation_date DESC ";
    private static final String SELECT_PAID_ORDERS_BY_USER_ID = "SELECT `order`.order_id,  " +
            "creation_date, payment_date, SUM(item_price.price*order_item.quantity) AS totalSum, " +
            "SUM(quantity) AS totalCount, `user`.login " +
            "FROM `order` " +
            "JOIN order_item ON `order`.order_id = order_item.order_id " +
            "JOIN item on order_item.item_id = item.item_id " +
            "JOIN item_price ON item.item_id = item_price.item_id AND order_item.add_date BETWEEN item_price.starts AND item_price.ends " +
            "JOIN user ON `order`.user_id = `user`.user_id " +
            "WHERE `order`.user_id=? AND payment_date IS NOT NULL AND creation_date IS NOT NULL " +
            "GROUP BY `order`.order_id " +
            "ORDER BY creation_date DESC ";
    private static final String INSERT_NEW_ORDER = "INSERT INTO `order` (user_id) VALUES(?)";
    private static final String ADD_ITEM_TO_ORDER = "INSERT INTO order_item (item_id, order_id, quantity, add_date) " +
            "VALUES (?, ?, ?, now()) ON DUPLICATE KEY UPDATE quantity = quantity + ?, add_date = now()";
    private static final String REMOVE_ITEM_FROM_ORDER = "DELETE FROM order_item WHERE order_id=? AND item_id=?";
    private static final String UPDATE_ORDER_PAYMENT = "UPDATE `order` SET payment_date=now() WHERE order_id=?";
    private static final String DELETE_ORDER = "DELETE FROM `order` WHERE order_id=? AND payment_date IS NULL";
    private static final String DELETE_LATE_ORDER = "DELETE FROM `order` WHERE order_id=? " +
            "AND creation_date < DATE_SUB(now(), INTERVAL 3 DAY) AND payment_date IS NULL";
    private static final String UPDATE_ORDER_STATUS = "UPDATE `order` SET creation_date=now() WHERE order_id=?";
    private static final String SELECT_CURRENT_ORDER= "SELECT `order`.order_id,  creation_date, payment_date, " +
            "SUM(item_price.price*order_item.quantity) AS totalSum, SUM(quantity) AS totalCount " +
            "FROM `order` JOIN order_item ON `order`.order_id=order_item.order_id " +
            "JOIN item on order_item.item_id = item.item_id " +
            "JOIN item_price ON item.item_id = item_price.item_id AND order_item.add_date BETWEEN item_price.starts AND item_price.ends  " +
            "WHERE user_id=? AND creation_date IS NULL";
    private static final String SELECT_ORDER_BY_ID= "SELECT  creation_date, payment_date, SUM(item_price.price*order_item.quantity) AS totalSum, " +
            "SUM(quantity) AS totalCount, `order`.user_id AS id, `user`.login AS `login` " +
            "FROM `order` JOIN order_item ON `order`.order_id=order_item.order_id " +
            "JOIN item on order_item.item_id = item.item_id " +
            "JOIN item_price ON item.item_id = item_price.item_id AND order_item.add_date BETWEEN item_price.starts AND item_price.ends  " +
            "JOIN user ON `order`.user_id = `user`.user_id " +
            "WHERE `order`.order_id=?";
    private static final String SELECT_CURRENT_ORDER_ID = "SELECT order_id FROM `order` WHERE user_id=? AND creation_date IS NULL";
    private static final String SELECT_DISTINCT_ITEMS_COUNT_IN_ORDER = "SELECT COUNT(item_id) AS `count` FROM order_item WHERE order_id=?";
    private static final String SELECT_ITEMS_BY_ORDER_ID = "SELECT item.item_id, `name`, size, color, item_price.price, quantity, image_path " +
            "FROM order_item " +
            "JOIN item ON order_item.item_id = item.item_id "+
            "JOIN item_price ON item.item_id = item_price.item_id AND order_item.add_date BETWEEN item_price.starts AND item_price.ends  " +
            "WHERE order_id = ?";
    private static final String SELECT_ORDER_COUNT_FOR_USER = "SELECT COUNT(order_id) AS `count` FROM user " +
            "LEFT JOIN `order` ON user.user_id = `order`.user_id " +
            "WHERE user.user_id=? AND creation_date IS NOT NULL";
    private static final String SELECT_UNPAID_ORDER_COUNT_FOR_USER = "SELECT COUNT(order_id) AS `count` FROM user " +
            "LEFT JOIN `order` ON user.user_id = `order`.user_id " +
            "WHERE user.user_id=? AND creation_date IS NOT NULL AND payment_date IS NULL";
    private static final String SELECT_PAID_ORDER_COUNT_FOR_USER = "SELECT COUNT(order_id) AS `count` FROM user " +
            "LEFT JOIN `order` ON user.user_id = `order`.user_id " +
            "WHERE user.user_id=? AND creation_date IS NOT NULL AND payment_date IS NOT NULL";
    private static final String SELECT_LATE_ORDER_COUNT_FOR_USER = "SELECT COUNT(order_id) AS `count` FROM user " +
            "LEFT JOIN `order` ON user.user_id = `order`.user_id " +
            "WHERE user.user_id=? AND creation_date < DATE_SUB(now(), INTERVAL 3 DAY) AND payment_date IS NULL";
    private static final String SELECT_ORDER_OWNER = "SELECT user_id FROM `order` WHERE order_id=?";

    private static OrderDao instance = new OrderDaoImpl();
    private OrderDaoImpl() {}
    public static OrderDao getInstance() {
        return instance;
    }

    @Override
    public List<Order> selectOrdersByUserId(long id) throws DaoException{
        return selectOrdersByQueryAndUserId(SELECT_ALL_ORDERS_BY_USER_ID, id);
    }

    @Override
    public List<Order> selectPaidOrdersByUserId(long id) throws DaoException {
        return selectOrdersByQueryAndUserId(SELECT_PAID_ORDERS_BY_USER_ID, id);
    }

    @Override
    public List<Order> selectUnpaidOrdersByUserId(long id) throws DaoException {
        return selectOrdersByQueryAndUserId(SELECT_UNPAID_ORDERS_BY_USER_ID, id);
    }

    @Override
    public List<Order> selectAllOrders() throws DaoException {
        return selectAllOrdersByQuery( SELECT_ALL_ORDERS);
    }

    @Override
    public List<Order> selectAllUnpaidOrders() throws DaoException {
        return selectAllOrdersByQuery(SELECT_ALL_UNPAID_ORDERS);
    }

    @Override
    public List<Order> selectAllPaidOrders() throws DaoException {
        return selectAllOrdersByQuery(SELECT_ALL_PAID_ORDERS);
    }

    public List<Order> selectAllOrdersByQuery(String query) throws DaoException {
        List<Order> orders = new ArrayList<>();
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(query)) {
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

    private List<Order> selectOrdersByQueryAndUserId(String query, long id) throws DaoException {
        List<Order> orders = new ArrayList<>();
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(query)) {
            st.setLong(1, id);
            ResultSet resultSet = st.executeQuery();
            while(resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt("order_id"));
                order.setUser(new User(id, resultSet.getString("login")));
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

    @Override
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
                order.setUser(new User(resultSet.getLong("id"), resultSet.getString("login")));
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
    public long selectCurrentOrderId(long userId) throws DaoException {
        long orderId = -1;
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
    public long insertNewOrder(long userId) throws DaoException {
        long result = -1;
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
    public boolean insertItemsToOrder(long itemId, int count, long orderId) throws DaoException {
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
    public boolean deleteItemsFromOrder(long itemId, long orderId) throws DaoException {
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
        int count = -1;
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
        int count = -1;
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
        int count = -1;
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
        int count = -1;
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
        int count = -1;
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
            PreparedStatement st = cn.prepareStatement(SELECT_ITEMS_BY_ORDER_ID)) {
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
    public long selectOrderOwnerId(long orderId) throws DaoException {
        long id = -1;
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
