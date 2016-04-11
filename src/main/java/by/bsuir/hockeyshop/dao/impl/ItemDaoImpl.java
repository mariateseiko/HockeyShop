package by.bsuir.hockeyshop.dao.impl;

import by.bsuir.hockeyshop.dao.ItemDao;
import by.bsuir.hockeyshop.entity.Item;
import by.bsuir.hockeyshop.entity.ItemStatus;
import by.bsuir.hockeyshop.entity.ItemType;
import by.bsuir.hockeyshop.dao.DaoException;
import by.bsuir.hockeyshop.pool.ConnectionPool;
import by.bsuir.hockeyshop.managers.ConfigurationManager;
import org.apache.log4j.Logger;

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
 * This implementation is based on JDBC and MySQL. It is also a singleton. Connections are taken and returned to the pool
 * in each method.
 */
public class ItemDaoImpl implements ItemDao {
    private static final String SELECT_ITEMS_BY_TYPE_ORDER_BY_PRICE_DESC = "SELECT item.item_id, item.name, item_status.name AS `status`, " +
            "size, color, item_price.price, image_path " +
            "FROM item " +
            "JOIN item_type ON item.type_id = item_type.type_id " +
            "JOIN item_status ON item.status_id = item_status.status_id " +
            "JOIN item_price ON item.item_id = item_price.item_id AND now() < item_price.ends " +
            "WHERE item_type.name = ? ORDER BY price DESC LIMIT ?, ?";
    private static final String SELECT_ITEMS_BY_TYPE_ORDER_BY_PRICE_ASC = "SELECT item.item_id, item.name, item_status.name AS `status`, " +
            "size, color, item_price.price, image_path " +
            "FROM item " +
            "JOIN item_status ON item.status_id = item_status.status_id " +
            "JOIN item_type ON item.type_id = item_type.type_id " +
            "JOIN item_price ON item.item_id = item_price.item_id AND now() < item_price.ends " +
            "WHERE item_type.name = ? ORDER BY price ASC LIMIT ?, ?";
    private static final String SELECT_ITEMS_COUNT_BY_TYPE = "SELECT COUNT(item_id) AS count " +
            "FROM item " +
            "JOIN item_type ON item.type_id = item_type.type_id " +
            "WHERE item_type.name = ?";
    private static final String INSERT_ITEM = "INSERT INTO item (`name`, size, color, type_id, status_id, image_path, " +
            "additional_info, description) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_INITIAL_PRICE = "INSERT INTO item_price (item_id, price) VALUES (LAST_INSERT_ID(), ?)";
    private static final String INSERT_NEW_PRICE = "INSERT INTO item_price (item_id, price, `starts`) VALUES " +
            "(?, ?, (SELECT e FROM (SELECT DISTINCT MAX(`ends`) AS e FROM item_price WHERE item_id=?) AS temp))";
    private static final String SELECT_TYPE_ID_BY_NAME = "SELECT type_id FROM item_type " +
            "WHERE name=?";
    private static final String SELECT_STATUS_ID_BY_NAME = "SELECT status_id FROM item_status " +
            "WHERE name=?";
    private static final String UPDATE_END_DATE = "UPDATE item_price SET `ends` = now() WHERE `ends` > now() AND item_id=?";
    private static final String UPDATE_ITEM_STATUS = "UPDATE item SET status_id=? " +
            "WHERE item_id=?";
    private static final String SELECT_ITEM_BY_ID = "SELECT item.item_id, item.name, size, color, item_price.price, item_status.name AS status, item_type.name AS type, image_path, " +
            "additional_info, description " +
            "FROM item " +
            "JOIN item_type ON item.type_id = item_type.type_id "+
            "JOIN item_price ON item.item_id = item_price.item_id AND now() < item_price.ends " +
            "JOIN item_status ON item.status_id = item_status.status_id " +
            "WHERE item.item_id = ?";

    private static ItemDao instance = new ItemDaoImpl();
    private ItemDaoImpl() {}
    public static ItemDao getInstance() {
        return instance;
    }

    @Override
    public List<Item> selectItemsByTypeOrderedByPriceAsc(String type, int offset, int limit) throws DaoException {
        List<Item> items = new ArrayList<>();
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(SELECT_ITEMS_BY_TYPE_ORDER_BY_PRICE_ASC)) {
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
                item.setStatus(ItemStatus.valueOf(resultSet.getString("status")));
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
            PreparedStatement st = cn.prepareStatement(SELECT_ITEMS_BY_TYPE_ORDER_BY_PRICE_DESC)) {
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
                item.setStatus(ItemStatus.valueOf(resultSet.getString("status")));
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
            PreparedStatement st = cn.prepareStatement(SELECT_ITEMS_COUNT_BY_TYPE)) {
            st.setString(1, type);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return -1;
    }

    @Override
    public boolean insertItem(Item item) throws DaoException {
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement stItem = cn.prepareStatement(INSERT_ITEM);
            PreparedStatement stPrice = cn.prepareStatement(INSERT_INITIAL_PRICE)) {
            cn.setAutoCommit(false);
            stItem.setString(1, item.getName());
            stItem.setString(2, item.getSize());
            stItem.setString(3, item.getColor());
            stItem.setLong(4, selectTypeIdByName(item.getType()));
            stItem.setLong(5, selectStatusIdByName(item.getStatus()));
            stItem.setString(6, item.getImagePath());
            stItem.setString(7, item.getAdditionalInfo());
            stItem.setString(8, item.getDescription());
            stItem.executeUpdate();
            stPrice.setInt(1, item.getPrice());
            stPrice.executeUpdate();
            cn.commit();
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return true;
    }


    long selectTypeIdByName(ItemType type) throws SQLException {
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(SELECT_TYPE_ID_BY_NAME)) {
            st.setString(1, type.toString());
            ResultSet resultSet = st.executeQuery();
            resultSet.next();
            return resultSet.getLong("type_id");
        }
    }

    long selectStatusIdByName(ItemStatus status) throws SQLException {
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(SELECT_STATUS_ID_BY_NAME)) {
            st.setString(1, status.toString());
            ResultSet resultSet = st.executeQuery();
            resultSet.next();
            return resultSet.getLong("status_id");
        }
    }

    @Override
    public boolean updateItemPrice(long id, int price) throws DaoException {
        Connection cn = ConnectionPool.getInstance().takeConnection();
        try (PreparedStatement stNew = cn.prepareStatement(INSERT_NEW_PRICE);
            PreparedStatement stOld = cn.prepareStatement(UPDATE_END_DATE)){
            cn.setAutoCommit(false);
            stOld.setLong(1, id);
            stOld.executeUpdate();
            stNew.setLong(1, id);
            stNew.setInt(2, price);
            stNew.setLong(3, id);
            stNew.executeUpdate();
            cn.commit();
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return true;
    }

    @Override
    public boolean updateItemStatus(long id, ItemStatus status) throws DaoException {
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(UPDATE_ITEM_STATUS)) {
            st.setLong(1, selectStatusIdByName(status));
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
        Item item = null;
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(SELECT_ITEM_BY_ID)) {
            st.setLong(1, id);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                item = new Item();
                item.setId(Long.parseLong(resultSet.getString("item_id")));
                item.setName(resultSet.getString("name"));
                item.setSize(resultSet.getString("size"));
                item.setColor(resultSet.getString("color"));
                item.setPrice(resultSet.getInt("price"));
                item.setStatus(ItemStatus.valueOf(resultSet.getString("status")));
                item.setType(ItemType.valueOf(resultSet.getString("type")));
                item.setImagePath(ConfigurationManager.getProperty("path.items")
                        + File.separator + resultSet.getString("image_path"));
                item.setAdditionalInfo(resultSet.getString("additional_info"));
                item.setDescription(resultSet.getString("description"));
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return item;
    }
}
