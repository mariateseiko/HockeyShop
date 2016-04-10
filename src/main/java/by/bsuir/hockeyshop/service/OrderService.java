package by.bsuir.hockeyshop.service;

import by.bsuir.hockeyshop.entity.Order;

import java.util.List;

/**
 * Represents an interface of a service providing order-related actions
 */
public interface OrderService {
    /**
     * Returns a list of {@link Order} orders of a specified user
     * @param userId id of the user, owning the requested orders
     * @return list of user's orders
     * @throws ServiceException if exception occurred on an underlying level
     */
    List<Order> selectOrdersByUser(long userId) throws ServiceException;

    /**
     * Selects a list of all paid or unpaid orders by a specified user
     * @param userId id of the user, owning the requested orders
     * @param paid {@code true} for paid orders, {@code false} for unpaid
     * @return a list of orders by a specified user with a given payment status
     * @throws ServiceException if exception occurred on an underlying level
     */
    List<Order> selectOrdersByUser(long userId, boolean paid) throws ServiceException;

    /**
     * Selects all orders made by all users
     * @return a list of all orders by all users
     * @throws ServiceException if exception occurred on an underlying level
     */
    List<Order> selectAllOrders() throws ServiceException;

    /**
     * Selects a list of all paid or unpaid orders by a all users
     * @return a list of orders
     * @throws ServiceException if exception occurred on an underlying level
     */
    List<Order> selectAllOrdersByPayment(boolean payed) throws ServiceException;

    /**
     * Submits an order with a specified id to the system. Date and time of submission are saved as creation date of the order.
     * After submission the order can't be modified, it can only be paid or deleted.
     * @param orderId id of the order to submit
     * @return {@code true} if submitted successfully, {@code false} if order doesn't contain any items or failed to be submitted
     * @throws ServiceException if exception occurred on an underlying level
     */
    boolean submitOrder(long orderId) throws ServiceException;

    /**
     * Adds a specified count of items with a given id to the order, identified by id
     * @param itemId id of the item to add
     * @param count count of items
     * @param userId id of the order to add to
     * @return {@code true} if added successfully
     * @throws ServiceException if exception occurred on an underlying level
     */
    boolean addItemsToOrder(long itemId, int count, long userId) throws ServiceException;

    /**
     * Removes all items with a given id from the specified order
     * @param itemId id of the item
     * @param userId id of the user
     * @return {@code true} if removed successfully
     * @throws ServiceException if exception occurred on an underlying level
     */
    boolean removeItemFromOrder(long itemId, long userId) throws ServiceException;

    /**
     * Processes payment for specified order
     * @param orderId id of the order to be paid for
     * @param card credit card credentials
     * @return {@code true} if payment succeeded
     * @throws ServiceException if exception occurred on an underlying level
     */
    boolean payForOrder(long orderId, String card) throws ServiceException;

    /**
     * Deletes an order with a given id.
     * @param orderId id of the order to delete
     * @param userId id of the user asking for order deletion
     * @return {@code true} if successfully deleted
     * @throws ServiceException if exception occurred on an underlying level
     */
    boolean deleteOrder(long orderId, long userId) throws ServiceException;

    /**
     * Deletes a late order by an admin request
     * @param orderId id of the order to delete
     * @return {@code true} if deleted successfully, {@code false} if failed to delete, e.g. order is not late
     * @throws ServiceException if exception occurred on an underlying level
     */
    boolean deleteLateOrder(long orderId) throws ServiceException;

    /**
     * Selects an order with a given id
     * @param orderId id of the order to view
     * @return a corresponding {@link Order} object
     * @throws ServiceException if exception occurred on an underlying level
     */
    Order selectOrder(long orderId) throws ServiceException;

    /**
     * Selects current order(e.g. cart) of the user with the given id
     * @param userId id of the order's owner
     * @return a corresponding {@link Order} object
     * @throws ServiceException if exception occurred on an underlying level
     */
    Order selectCurrentOrder(long userId) throws ServiceException;

    /**
     * Selects id of the user, owning the order
     * @param orderId id of the order
     * @return id of the user, owning the order
     * @throws ServiceException if exception occurred on an underlying level
     */
    long selectOrderOwnerId(long orderId) throws ServiceException;
}
