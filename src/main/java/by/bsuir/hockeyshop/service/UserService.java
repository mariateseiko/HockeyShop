package by.bsuir.hockeyshop.service;

import by.bsuir.hockeyshop.entity.User;

import java.util.List;

/**
 * Represents an interface of a service for user-related actions
 */
public interface UserService {
    /**
     * Attempts to authenticate and authorize a user with a given login and password
     * @param login user's login
     * @param password user's password
     * @return {@code User} object with id and role or null, if credentials are invalid
     * @throws ServiceException if DaoException occurred
     */
    User loginUser(String login, String password) throws ServiceException;

    /**
     * Attempts to register a new user with given personal info. Login should be unique in the system
     * @param login user's login
     * @param password user's password
     * @param email user's email
     * @return true if registration succeeded, false if user with such login already exists
     * @throws ServiceException if DaoException occurred
     */
    boolean registerUser(String login, String password, String email) throws ServiceException;

    /**
     * Bans or unbans a specified user
     * @param userId id of the user to change banned status
     * @param banned {@code true} to ban, {@code false} to unban
     * @return true if status successfully changed
     * @throws ServiceException if DaoException occurred
     */
    boolean changeUserBanStatus(long userId, boolean banned) throws ServiceException;

    /**
     * Retrieves a user with a specified id
     * @param userId id of the user to retrieve
     * @return a user with the specified id or null if such doesn't exist
     * @throws ServiceException if DaoException occurred
     */
    User selectUser(long userId) throws ServiceException;

    /**
     * Retrieves a list of all client users
     * @return a list of users from the given offset
     * @throws ServiceException if DaoException occurred
     */
    List<User> selectUsers() throws ServiceException;
}
