package by.bsuir.hockeyshop.dao.impl;

import by.bsuir.hockeyshop.dao.UserDao;
import by.bsuir.hockeyshop.entity.User;
import by.bsuir.hockeyshop.entity.UserRole;
import by.bsuir.hockeyshop.dao.DaoException;
import by.bsuir.hockeyshop.pool.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 *
 * This implementation is based on JDBC and MySQL. It is also a singleton. Connections are taken and returned to the pool
 * in each method.
 */

public class UserDaoImpl implements UserDao {
    private final static String SELECT_USERS = "SELECT * FROM user " +
            "JOIN user_role on user.role_id = user_role.role_id " +
            "WHERE user_role.name='CLIENT'";
    private final static String SELECT_USER_BY_ID = "SELECT * FROM user "
            +"JOIN user_role on user.role_id = user_role.role_id "
            +"WHERE user_id=? ";
    private final static String SELECT_USER_BY_LOGIN_PASSWORD = "SELECT * FROM user "
            +"JOIN user_role on user.role_id = user_role.role_id "
            +"WHERE login=? AND password=?";
    private final static String INSERT_USER = "INSERT INTO user (login, password, email, role_id) VALUES(?, ?, ?, ?)";
    private final static String SELECT_ROLE_ID_BY_NAME = "SELECT role_id FROM user_role WHERE name=?";
    private final static String UPDATE_USER_STATUS = "UPDATE user SET banned=? WHERE user_id=?";

    public static UserDao getInstance() {
        return instance;
    }
    private static UserDao instance = new UserDaoImpl();
    private UserDaoImpl() {}

    @Override
    public User selectUserById(long id) throws DaoException {
        User user = new User();
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(SELECT_USER_BY_ID)) {
            st.setLong(1, id);
            ResultSet resultSet = st.executeQuery();
            resultSet.next();
            user.setId(resultSet.getLong("user_id"));
            user.setLogin(resultSet.getString("login"));
            user.setEmail(resultSet.getString("email"));
            user.setRole(UserRole.valueOf(resultSet.getString("user_role.name").toUpperCase()));
            user.setBanned(resultSet.getBoolean("banned"));
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return user;
    }

    @Override
    public boolean updateUserBannedStatus(long userId, boolean banStatus) throws DaoException {
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(UPDATE_USER_STATUS)) {
            st.setBoolean(1, banStatus);
            st.setLong(2, userId);
            if (st.executeUpdate() == 0) {
                return false;
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return true;
    }

    @Override
    public boolean insertUser(User user) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(INSERT_USER)) {
            st.setString(1, user.getLogin());
            st.setString(2, user.getPassword());
            st.setString(3, user.getEmail());
            st.setInt(4, selectRoleIdByName(user.getRole().toString().toUpperCase()));
            st.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return true;
    }

    int selectRoleIdByName(String name) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(SELECT_ROLE_ID_BY_NAME)) {
            st.setString(1, name);
            ResultSet resultSet = st.executeQuery();
            resultSet.next();
            return resultSet.getInt("role_id");
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
    }

    @Override
    public User selectUserByLoginPassword(String login, String password) throws DaoException{
        User user = null;
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(SELECT_USER_BY_LOGIN_PASSWORD)) {
            st.setString(1, login);
            st.setString(2, String.valueOf(password));
            ResultSet resultSet = st.executeQuery();
            if(resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("user_id"));
                user.setLogin(resultSet.getString("login"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(UserRole.valueOf(resultSet.getString("user_role.name").toUpperCase()));
                user.setBanned(resultSet.getBoolean("banned"));
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return user;
    }

    @Override
    public List<User> selectUsers() throws DaoException {
        List<User> users = new ArrayList<>();
        try(Connection cn = ConnectionPool.getInstance().takeConnection();
            PreparedStatement st = cn.prepareStatement(SELECT_USERS)) {
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("user_id"));
                user.setLogin(resultSet.getString("login"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(UserRole.valueOf(resultSet.getString("user_role.name").toUpperCase()));
                user.setBanned(resultSet.getBoolean("banned"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new DaoException("Request to database failed", e);
        }
        return users;
    }
}
