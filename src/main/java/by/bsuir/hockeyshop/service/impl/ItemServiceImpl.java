package by.bsuir.hockeyshop.service.impl;

import by.bsuir.hockeyshop.dao.ItemDao;
import by.bsuir.hockeyshop.dao.impl.ItemDaoImpl;
import by.bsuir.hockeyshop.entity.Item;
import by.bsuir.hockeyshop.entity.ItemStatus;
import by.bsuir.hockeyshop.entity.ItemType;
import by.bsuir.hockeyshop.dao.DaoException;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.managers.ConfigurationManager;
import by.bsuir.hockeyshop.service.ItemService;

import java.util.List;

public class ItemServiceImpl implements ItemService {
    private static ItemDao itemDao;
    private static ItemService instance = new ItemServiceImpl();

    private ItemServiceImpl() {
        itemDao = ItemDaoImpl.getInstance();
    }

    public static ItemService getInstance() {
        return instance;
    }

    @Override
    public boolean updateItemPrice(long id, int price) throws ServiceException {
        try {
            return itemDao.updateItemPrice(id, price);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean updateItemStatus(long id, ItemStatus status) throws ServiceException {
        try {
            return itemDao.updateItemStatus(id, status);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Item> getItemsByTypeSortedByPriceAsc(ItemType type, int offset, int limit) throws ServiceException {
        try {
            String itemType = type.toString();
            return itemDao.selectItemsByTypeOrderedByPriceAsc(itemType, offset, limit);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Item> getItemsByTypeSortedByPriceDesc(ItemType type, int offset, int limit) throws ServiceException {
        try {
            String itemType = type.toString();
            return itemDao.selectItemsByTypeOrderedByPriceDesc(itemType, offset, limit);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Item getItem(long id) throws ServiceException {
        Item item;
        try {
            item = itemDao.selectItemById(id);
            if (item != null) {
                if (item.getImagePath() == null) {
                    item.setImagePath(ConfigurationManager.getProperty("path.image.default"));
                }
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return item;
    }

    @Override
    public int getItemsCountByType(ItemType type) throws ServiceException {
        try {
            return itemDao.selectItemsCountByType(type.toString());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean addItem(Item item) throws ServiceException {
        try {
            return itemDao.insertItem(item);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }


}
