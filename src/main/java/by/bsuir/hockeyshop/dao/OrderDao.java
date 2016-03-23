package by.bsuir.hockeyshop.dao;

import by.bsuir.hockeyshop.entity.Item;
import by.bsuir.hockeyshop.entity.Order;

import java.util.List;
import java.util.Map;

/**
 * Represents an interface for retrieving order-related data. Contains all methods, required for getting such data from the
 * storage, e.g. database
 */
public interface OrderDao {
    /**
     * Retrieves a list of all user's orders, from a specified offset with a given limit
     * @param userId id of the user
     * @param offset offset from the start of the list of all user's orders
     * @param limit max number of orders in list
     * @return list of user's orders or {@code null} if user has none
     * @throws DaoException  if failed to retrieve data from the storage due to technical problems
     */
    List<Order> selectOrdersByUserId(long userId, int offset, int limit) throws DaoException;

    /**
     * Retrieves a list of all paid user's orders, from a specified offset with a given limit
     * @param userId id of the user
     * @param offset offset from the start of the list of all paid user's orders
     * @param limit max number of orders in list
     * @return list of paid user's orders or {@code null} if user has none
     * @throws DaoException  if failed to retrieve data from the storage due to technical problems
     */
    List<Order> selectPaidOrdersByUserId(long userId, int offset, int limit) throws DaoException;

    /**
     * Retrieves a list of all unpaid user's orders, from a specified offset with a given limit
     * @param userId id of the user
     * @param offset offset from the start of the list of all unpaid user's orders
     * @param limit max number of orders in list
     * @return list of unpaid user's orders or {@code null} if user has none
     * @throws DaoException  if failed to retrieve data from the storage due to technical problems
     */
    List<Order> selectUnpaidOrdersByUserId(long userId, int offset, int limit) throws DaoException;

    /**
     * Retrieves a list orders made by all clients from a specified offset with a given limit
     * @param offset offset from the start of the list of all users' orders
     * @param limit max number of orders in list
     * @return list of all orders or {@code null} if there are none
     * @throws DaoException  if failed to retrieve data from the storage due to technical problems
     */
    List<Order> selectAllOrders(int offset, int limit) throws DaoException;

    /**
     * Retrieves a list of unpaid orders made by all clients from a specified offset with a given limit
     * @param offset offset from the start of the list of all unpaid users' orders
     * @param limit max number of orders in list
     * @return list of all unpaid orders or {@code null} if there are none
     * @throws DaoException  if failed to retrieve data from the storage due to technical problems
     */
    List<Order> selectAllUnpaidOrders(int offset, int limit) throws DaoException;

    /**
     * Retrieves a list of paid orders made by all clients from a specified offset with a given limit
     * @param offset offset from the start of the list of all paid users' orders
     * @param limit max number of orders in list
     * @return list of all paid orders or {@code null} if there are none
     * @throws DaoException  if failed to retrieve data from the storage due to technical problems
     */
    List<Order> selectAllPaidOrders(int offset, int limit) throws DaoException;

    /**
     * Retrieves current(unsubmitted) order for a specified user
     * @param userId id of the user, owning the order
     * @return user's current order or {@code null} if there is none
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    Order selectCurrentOrder(long userId) throws DaoException;

    /**
     * Retrieves an order by its id
     * @param orderId order's id
     * @return order corresponding to the id or {@code null} if there is none
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    Order selectOrder(long orderId) throws DaoException;

    /**
     * Retrieves id of the user's current order
     * @param userId id of the user, owning the order
     * @return order's id or {@code null} if there is no current order
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    Long selectCurrentOrderId(long userId) throws DaoException;

    /**
     * Places new current order for a specified user
     * @param userId id of the user, owning the order
     * @return order's id or {@code null} if insert failed
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    Long insertNewOrder(long userId) throws DaoException;

    /**
     * Adds an item with a specified id of a specified count to a given order
     * @param itemId id of the item to add
     * @param count count of items
     * @param orderId id of the order
     * @return {@code true} if inserted successfully, {@code false} if insert failed
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    boolean addItemsToOrder(long itemId, int count, long orderId) throws DaoException;

    /**
     * Removes all items with a specified id from given order
     * @param itemId id of the item to remove
     * @param orderId id of the order
     * @return {@code true} if removed successfully, {@code false} if delete failed
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    boolean removeItemsFromOrder(long itemId, long orderId) throws DaoException;

    /**
     * Marks an order as paid and sets date and time of payment
     * @param orderId order to pay for
     * @return {@code true} if updated successfully, {@code false} if update failed
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    boolean updateOrderPayment(long orderId) throws DaoException;

    /**
     * Deletes a specified order
     * @param orderId id of the order to delete
     * @return {@code true} if deleted successfully, {@code false} if delete failed
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    boolean deleteOrder(long orderId) throws DaoException;

    /**
     * Updates user's current order to submitted
     * @param userId of the user, owning the order
     * @return {@code true} if updated successfully, {@code false} if update failed
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    boolean updateOrderStatusToSubmitted(long userId) throws DaoException;

    /**
     * Retrieves count of all submitted orders of a specified user
     * @param userId id of the user
     * @return count of user's submitted orders
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    int selectSubmittedOrdersCountByUser(long userId) throws DaoException;

    /**
     * Retrieves count of late orders of a specified user
     * @param userId id of the user
     * @return count of user's late orders
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    int selectLateOrdersCountByUser(long userId) throws DaoException;

    /**
     * Retrieves count of paid orders of a specified user
     * @param userId id of the user
     * @return count of user's paid orders
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    int selectPaidOrdersCountByUser(long userId) throws DaoException;

    /**
     * Retrieves count of unpaid orders of a specified user
     * @param userId id of the user
     * @return count of user's unpaid orders
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    int selectUnpaidOrdersCountByUser(long userId) throws DaoException;

    /**
     * Retrieves count of items in a specified order
     * @param orderId id of the order
     * @return count items in order
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    int selectItemCountInOrder(long orderId) throws DaoException;

    /**
     * Retrieves a map, containing all items from a specified order. Map's key is an item, and count of this item in order
     * is its corresponding value.
     * @param orderId id of the order
     * @return map, containing items from the order
     * @throws DaoException
     */
    Map<Item, Integer> getItemsByOrderId(long orderId) throws DaoException;

    Long selectOrderOwnerId(long orderId) throws DaoException;
}
