package by.bsuir.hockeyshop.service;

import by.bsuir.hockeyshop.entity.Item;
import by.bsuir.hockeyshop.entity.ItemStatus;
import by.bsuir.hockeyshop.entity.ItemType;

import java.util.List;

/**
 * Represents an interface of a service for item-related actions
 */
public interface ItemService {
    /**
     * Adds a new item to the catalog
     * @param item item to be added
     * @return {@code true} if added successfully
     * @throws ServiceException if exception occurred on an underlying level
     */
    boolean addItem(Item item) throws ServiceException;

    /**
     * Updates price of the specified item
     * @param itemId id of the item to update
     * @param price new price
     * @return {@code true} if updated successfully
     * @throws ServiceException if exception occurred on an underlying level
     */
    boolean updateItemPrice(long itemId, int price) throws ServiceException;

    /**
     * Updates status of the specified item
     * @param itemId id of the item to update
     * @param status new status
     * @return {@code true} if updated successfully
     * @throws ServiceException if exception occurred on an underlying level
     */
    boolean updateItemStatus(long itemId, ItemStatus status) throws ServiceException;

    /**
     * Selects a list of items of specified type from the catalog sorted in ascending order
     * @param type type of the items to select
     * @param offset offset from the start of the catalog, 0 - for selecting from the start
     * @param limit maximum number of items in the list
     * @return a list of items
     * @throws ServiceException if exception occurred on an underlying level
     */
    List<Item> selectItemsByTypeSortedByPriceAsc(ItemType type, int offset, int limit) throws ServiceException;

    /**
     * Selects a list of items of specified type from the catalog sorted in ascending order
     * @param type type of the items to select
     * @param offset offset from the start of the catalog, 0 - for selecting from the start
     * @param limit maximum number of items in the list
     * @return a list of items
     * @throws ServiceException if exception occurred on an underlying level
     */
    List<Item> selectItemsByTypeSortedByPriceDesc(ItemType type, int offset, int limit) throws ServiceException;

    /**
     * Selects an by a given id
     * @param id id of the item
     * @return corresponding item
     * @throws ServiceException if exception occurred on an underlying level
     */
    Item selectItem(long id) throws ServiceException;

    /**
     * Returns a number of items in the catalog with a given type
     * @param type type of the items to count
     * @return count of the items
     * @throws ServiceException if exception occurred on an underlying level
     */
    int selectItemsCountByType(ItemType type) throws ServiceException;
}
