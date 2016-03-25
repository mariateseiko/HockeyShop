package by.bsuir.hockeyshop.service;

import by.bsuir.hockeyshop.entity.Item;
import by.bsuir.hockeyshop.entity.ItemStatus;
import by.bsuir.hockeyshop.entity.ItemType;

import java.util.List;

public interface ItemService {
    boolean addItem(Item item) throws ServiceException;
    boolean updateItemPrice(long id, int price) throws ServiceException;
    boolean updateItemStatus(long id, ItemStatus status) throws ServiceException;
    List<Item> selectItemsByTypeSortedByPriceAsc(ItemType type, int offset, int limit) throws ServiceException;
    List<Item> selectItemsByTypeSortedByPriceDesc(ItemType type, int offset, int limit) throws ServiceException;
    Item selectItem(long id) throws ServiceException;
    int selectItemsCountByType(ItemType type) throws ServiceException;
}
