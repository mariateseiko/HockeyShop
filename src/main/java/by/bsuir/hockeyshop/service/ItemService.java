package by.bsuir.hockeyshop.service;

import by.bsuir.hockeyshop.entity.Item;
import by.bsuir.hockeyshop.entity.ItemStatus;
import by.bsuir.hockeyshop.entity.ItemType;

import java.util.List;

public interface ItemService {
    boolean addItem(Item item) throws ServiceException;
    boolean updateItemPrice(long id, int price) throws ServiceException;
    boolean updateItemStatus(long id, ItemStatus status) throws ServiceException;
    List<Item> getItemsByTypeSortedByPriceAsc(ItemType type, int offset, int limit) throws ServiceException;
    List<Item> getItemsByTypeSortedByPriceDesc(ItemType type, int offset, int limit) throws ServiceException;
    Item getItem(long id) throws ServiceException;
    int getItemsCountByType(ItemType type) throws ServiceException;
}
