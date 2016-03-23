package by.bsuir.hockeyshop.service;

import by.bsuir.hockeyshop.entity.User;

import java.util.List;

public interface UserService {
    User loginUser(String login, String password) throws ServiceException;
    boolean registerUser(String login, String password, String email) throws ServiceException;
    boolean changeUserBanStatus(long userId, boolean banned) throws ServiceException;
    User selectUser(long user_id) throws ServiceException;
    List<User> selectUsers(int limit, int offset) throws ServiceException;
}
