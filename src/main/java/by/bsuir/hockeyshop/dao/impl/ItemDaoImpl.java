package by.bsuir.hockeyshop.dao.impl;

import by.bsuir.hockeyshop.dao.ItemDao;
import by.bsuir.hockeyshop.entity.Item;
import by.bsuir.hockeyshop.entity.ItemStatus;
import by.bsuir.hockeyshop.entity.ItemType;
import by.bsuir.hockeyshop.dao.DaoException;
import by.bsuir.hockeyshop.pool.ConnectionPool;
import by.bsuir.hockeyshop.managers.ConfigurationManager;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 *
 * This implementation is based on JDBC and MySQL. It is also a singleton. Connecetions are taken and returned to the pool
 * in each method.
 */
public class ItemDaoImpl implements ItemDao {
    static final String GET_ITEMS_BY_TYPE_ORDER_BY_PRICE_DESC = "SELECT item_id, item.name, size, color, price, image_path " +
            "FROM item " +
            "JOIN item_status ON item.status_id = item_status.status_id " +
            "JOIN item_type ON item.type_id = item_type.type_id " +
            "WHERE item_type.name = ? AND item_status.name = 'IN_STOCK' ORDER BY price DESC LIMIT ?, ?";
    static final String GET_ITEMS_BY_TYPE_ORDER_BY_PRICE_ASC = "SELECT item_id, item.name, size, color, price, image_path " +
            "FROM item " +
            "JOIN item_status ON item.status_id = item_status.status_id " +
            "JOIN item_type ON item.type_id = item_type.type_id " +
            "WHERE item_type.name = ? AND item_status.name = 'IN_STOCK' ORDER BY price ASC LIMIT ?, ?";
    static final String GET_ITEMS_COUNT_BY_TYPE = "SELECT COUNT(item_id) AS count " +
            "FROM item " +
            "JOIN item_type ON item.type_id = item_type.type_id " +
            "WHERE item_type.name = ?";
    static final String INSERT_ITEM = "INSERT INTO item (name, size, color, price, type_id, status_id, image_path, " +
            "additional_info, description) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    static final String GET_TYPE_ID_BY_NAME = "SELECT type_id FROM item_type " +
            "WHERE name=?";
    static final String GET_STATUS_ID_BY_NAME = "SELECT status_id FROM item_status " +
            "WHERE name=?";
    static final String UPDATE_ITEM_PRICE = "UPDATE item SET price=? " +
            "WHERE item_id=? AND item_id NOT IN " +
            "(SELECT item_id FROM " +
            "(SELECT DISTINCT item.item_id FROM item " +
            "JOIN order_item ON item.item_id=order_item.item_id " +
            "JOIN `order` ON order_item.order_id = `order`.order_id " +
            "WHERE payment_date = 0) AS temp)";
    static final String UPDATE_ITEM_STATUS = "UPDATE item SET status_id=? " +
            "WHERE item_id=?";
    static final String GET_ITEM_BY_ID = "SELECT item_id, item.name, size, color, price, item_status.name AS status, item_type.name AS type, image_path, " +
            "additional_info, description " +
            "FROM item " +
            "JOIN item_type ON item.type_id = item_type.type_id "+
            "JOIN item_status ON item.status_id = item_status.status_id " +
            "WHERE item_id = ?";


    private static ItemDao instance = new ItemDaoImpl();

    private ItemDaoImpl() {}

    public static ItemDao getInstance() {
        return instance;
    }

    @Override
    public List<Item> selectItemsByTypeOrderedByPriceAsc(String type, int offset, int limit) throws DaoException {
        List<Item> items = new ArrayList<>();
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(GET_ITEMS_BY_TYPE_ORDER_BY_PRICE_ASC)) {
            st.setString(1, type);
            st.setInt(2, offset);
            st.setInt(3, limit);
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
                items.add(item);
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return items;
    }

    @Override
    public List<Item> selectItemsByTypeOrderedByPriceDesc(String type, int offset, int limit) throws DaoException {
        List<Item> items = new ArrayList<>();
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(GET_ITEMS_BY_TYPE_ORDER_BY_PRICE_DESC)) {
            st.setString(1, type);
            st.setInt(2, offset);
            st.setInt(3, limit);
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
                items.add(item);
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return items;
    }

    @Override
    public int selectItemsCountByType(String type) throws DaoException {
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(GET_ITEMS_COUNT_BY_TYPE)) {
            st.setString(1, type);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return 0;
    }

    @Override
    public boolean insertItem(Item item) throws DaoException {
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(INSERT_ITEM)) {
            st.setString(1, item.getName());
            st.setString(2, item.getSize());
            st.setString(3, item.getColor());
            st.setInt(4, item.getPrice());
            st.setLong(5, getTypeIdByName(item.getType()));
            st.setLong(6, getStatusIdByName(item.getStatus()));
            st.setString(7, item.getImagePath());
            st.setString(8, item.getAdditionalInfo());
            st.setString(9, item.getDescription());
            if (st.executeUpdate() == 0) {
                return false;
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return true;
    }


    long getTypeIdByName(ItemType type) throws SQLException {
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(GET_TYPE_ID_BY_NAME)) {
            st.setString(1, type.toString());
            ResultSet resultSet = st.executeQuery();
            resultSet.next();
            return resultSet.getInt("type_id");
        }
    }

    long getStatusIdByName(ItemStatus status) throws SQLException {
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(GET_STATUS_ID_BY_NAME)) {
            st.setString(1, status.toString());
            ResultSet resultSet = st.executeQuery();
            resultSet.next();
            return resultSet.getInt("status_id");
        }
    }

    @Override
    public boolean updateItemPrice(long id, int price) throws DaoException {
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(UPDATE_ITEM_PRICE)) {
            st.setInt(1, price);
            st.setLong(2, id);
            if (st.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return false;
    }

    @Override
    public boolean updateItemStatus(long id, ItemStatus status) throws DaoException {
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(UPDATE_ITEM_STATUS)) {
            st.setLong(1, getStatusIdByName(status));
            st.setLong(2, id);
            if (st.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return false;
    }

    @Override
    public Item selectItemById(long id) throws DaoException {
        Item item = new Item();
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(GET_ITEM_BY_ID)) {
            st.setLong(1, id);
            ResultSet resultSet = st.executeQuery();
            resultSet.next();
            item.setId(Long.parseLong(resultSet.getString("item_id")));
            item.setName(resultSet.getString("name"));
            item.setSize(resultSet.getString("size"));
            item.setColor(resultSet.getString("color"));
            item.setPrice(resultSet.getInt("price"));
            item.setStatus(ItemStatus.valueOf(resultSet.getString("status")));
            item.setType(ItemType.valueOf(resultSet.getString("type")));
            item.setImagePath(ConfigurationManager.getProperty("path.items")
                    + File.separator+resultSet.getString("image_path"));
            item.setAdditionalInfo(resultSet.getString("additional_info"));
            item.setDescription(resultSet.getString("description"));
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return item;
    }
}
