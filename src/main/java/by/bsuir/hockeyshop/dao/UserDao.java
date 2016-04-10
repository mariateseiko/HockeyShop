package by.bsuir.hockeyshop.dao;


import by.bsuir.hockeyshop.entity.User;

import java.util.List;
/**
 * Represents an interface for retrieving user-related data. Contains all methods, required for getting such data from the
 * storage, e.g. database
 */
public interface UserDao {

    /**
     * Retrieves a user with given login and password hash.
     * @param login user's login
     * @param password password hashed by md5 algorithm
     * @return user with corresponding login and password or {@code null} if such user doesn't exist
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    User selectUserByLoginPassword(String login, String password) throws DaoException;

    /**
     * Inserts new user to the storage
     * @param user user to insert
     * @return {@code true} if item has been successfully inserted, {@code false} if insert failed
     * @throws DaoException if failed to insert data to the storage due to technical problems
     */
    boolean insertUser(User user) throws DaoException;

    /**
     * Retrieves a user with given id
     * @param userId id of the user
     * @return user with corresponding if or {@code null} if such user doesn't exist
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    User selectUserById(long userId) throws DaoException;

    /**
     * Updates user's banned status
     * @param userId id of the user
     * @param banStatus {@code true} to ban, or {@code false} to unban
     * @return {@code true} if item has been successfully updated, {@code false} if update failed
     * @throws DaoException if failed to update data from the storage due to technical problems
     */
    boolean updateUserBannedStatus(long userId, boolean banStatus) throws DaoException;

    /**
     * Retrieves a list of clients
     * @return list of users
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    List<User> selectUsers() throws DaoException;
}
