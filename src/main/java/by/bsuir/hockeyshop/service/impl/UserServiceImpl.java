package by.bsuir.hockeyshop.service.impl;

import by.bsuir.hockeyshop.dao.OrderDao;
import by.bsuir.hockeyshop.dao.UserDao;
import by.bsuir.hockeyshop.dao.impl.OrderDaoImpl;
import by.bsuir.hockeyshop.dao.impl.UserDaoImpl;
import by.bsuir.hockeyshop.entity.User;
import by.bsuir.hockeyshop.entity.UserRole;
import by.bsuir.hockeyshop.dao.DaoException;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.service.UserService;
import by.bsuir.hockeyshop.service.util.Hasher;

import java.util.List;

/**
 * {@inheritDoc}
 *
 * A singleton implementation of the {@link UserService} interface, using {@link UserDaoImpl} as an underlying level
 */
public class UserServiceImpl implements UserService {
    private static UserDao userDao = UserDaoImpl.getInstance();
    private static OrderDao orderDao = OrderDaoImpl.getInstance();
    private static UserService instance = new UserServiceImpl();

    private UserServiceImpl() { }

    public static UserService getInstance() {
        return instance;
    }

    @Override
    public User loginUser(String login, String password) throws ServiceException {
        User user;
        try {
            String hashedPassword = Hasher.md5Hash(password);
            user = userDao.selectUserByLoginPassword(login, hashedPassword);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return user;
    }

    @Override
    public boolean registerUser(String login, String password, String email) throws ServiceException {
        boolean result;
        try {
            String hashedPassword = Hasher.md5Hash(password);
            User user = new User();
            user.setLogin(login);
            user.setPassword(hashedPassword);
            user.setEmail(email);
            user.setRole(UserRole.CLIENT);
            result = userDao.insertUser(user);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return result;
    }

    @Override
    public boolean changeUserBanStatus(long userId, boolean banned) throws ServiceException {
        try {
            return userDao.updateUserBannedStatus(userId, banned);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User selectUser(long userId) throws ServiceException {
        User user;
        try {
            user = userDao.selectUserById(userId);
            user.setCountOfSubmittedOrders(orderDao.selectSubmittedOrdersCountByUser(user.getId()));
            user.setCountOfPaidOrders(orderDao.selectPaidOrdersCountByUser(user.getId()));
            user.setCountOfUnpaidOrders(orderDao.selectUnpaidOrdersCountByUser(user.getId()));
            user.setCountOfLateOrders(orderDao.selectLateOrdersCountByUser(user.getId()));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return user;
    }

    @Override
    public List<User> selectUsers() throws ServiceException{
        List<User> users;
        try {
            users = userDao.selectUsers();
            for (User user: users) {
                user.setCountOfSubmittedOrders(orderDao.selectSubmittedOrdersCountByUser(user.getId()));
                user.setCountOfPaidOrders(orderDao.selectPaidOrdersCountByUser(user.getId()));
                user.setCountOfUnpaidOrders(orderDao.selectUnpaidOrdersCountByUser(user.getId()));
                user.setCountOfLateOrders(orderDao.selectLateOrdersCountByUser(user.getId()));
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return users;
    }
}
