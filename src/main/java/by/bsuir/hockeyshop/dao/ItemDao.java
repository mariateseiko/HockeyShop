package by.bsuir.hockeyshop.dao;

import by.bsuir.hockeyshop.entity.Item;
import by.bsuir.hockeyshop.entity.ItemStatus;

import java.util.List;

/**
 * Represents an interface for retrieving item-related data. Contains all methods, required for getting such data from the
 * storage, e.g. database
 */
public interface ItemDao {
    /**
     * Retrieves a list of items of a specified {@param type} from {@param offset}. List's max limit is {@param limit}.
     * Items' order in the list is ascending by price.
     * @param type type of items to retrieve
     * @param offset offset from the start of the list off all items of the specified type
     * @param limit max number of items in the list
     * @return list of items
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    List<Item> selectItemsByTypeOrderedByPriceAsc(String type, int offset, int limit) throws DaoException;

    /**
     * Retrieves a list of items of a specified {@param type} from {@param offset}. List's max limit is {@param limit}.
     * Items' order in the list is descending by price.
     * @param type type of items to retrieve
     * @param offset offset from the start of the list off all items of the specified type
     * @param limit max number of items in the list
     * @return list of items
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    List<Item> selectItemsByTypeOrderedByPriceDesc(String type, int offset, int limit) throws DaoException;

    /**
     * Defines total number of items of a specified type
     * @param type type of items to count
     * @return total number of items
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    int selectItemsCountByType(String type) throws DaoException;

    /**
     * Adds new items to the storage
     * @param item item to insert
     * @return {@code true} if item has been successfully inserted, {@code false} if insert failed
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    boolean insertItem(Item item) throws DaoException;

    /**
     * Updates price of an existing item with a specified id to a new given value
     * @param id id of the item to be updated
     * @param price new price
     * @return {@code true} if item has been successfully updated, {@code false} if update failed
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    boolean updateItemPrice(long id, int price) throws DaoException;

    /**
     * Updates status of an existing item with a specified id to a new given value
     * @param id id of the item to be updated
     * @param status new status
     * @return {@code true} if item has been successfully update, {@code false} if update failed
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    boolean updateItemStatus(long id, ItemStatus status) throws DaoException;

    /**
     * Retrieves an item with a specified id
     * @param id id of the desired item
     * @return corresponding item or {@code null} if such doesn't exist
     * @throws DaoException if failed to retrieve data from the storage due to technical problems
     */
    Item selectItemById(long id) throws DaoException;
}
