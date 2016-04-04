package by.bsuir.hockeyshop.service;

import by.bsuir.hockeyshop.entity.Order;

import java.util.List;

public interface OrderService {
    List<Order> selectOrdersByUser(long userId, int offset, int limit) throws ServiceException;
    List<Order> selectOrdersByUser(long userId, boolean payed, int offset, int limit) throws ServiceException;
    List<Order> selectAllOrders(int offset, int limit) throws ServiceException;
    List<Order> selectAllOrdersByPayment(boolean payed, int offset, int limit) throws ServiceException;
    boolean submitOrder(long orderId) throws ServiceException;
    boolean addItemsToOrder(long itemId, int count, long userId) throws ServiceException;
    boolean removeItemFromOrder(long itemId, long userId) throws ServiceException;
    boolean payForOrder(long orderId, String card) throws ServiceException;
    boolean deleteOrder(long id) throws ServiceException;
    Order selectOrder(long orderId) throws ServiceException;
    Order selectCurrentOrder(long userId) throws ServiceException;
    Long selectOrderOwnerId(long orderId) throws ServiceException;
    boolean deleteLateOrder(long orderId) throws ServiceException;
}
