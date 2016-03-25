package by.bsuir.hockeyshop.service.impl;

import by.bsuir.hockeyshop.dao.DaoException;
import by.bsuir.hockeyshop.dao.OrderDao;
import by.bsuir.hockeyshop.dao.impl.OrderDaoImpl;
import by.bsuir.hockeyshop.entity.Item;
import by.bsuir.hockeyshop.entity.Order;
import by.bsuir.hockeyshop.service.OrderService;
import by.bsuir.hockeyshop.service.ServiceException;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private static OrderService instance = new OrderServiceImpl();

    private OrderServiceImpl() {
        orderDao = OrderDaoImpl.getInstance();
    }

    public static OrderService getInstance() {
        return instance;
    }

    @Override
    public List<Order> selectOrdersByUser(long userId, int offset, int limit) throws ServiceException {
        try {
            return orderDao.selectOrdersByUserId(userId, offset, limit);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> selectOrdersByUser(long userId, boolean payed, int offset, int limit) throws ServiceException {
        List<Order> orders;
        try {
            if (payed) {
                orders = orderDao.selectPaidOrdersByUserId(userId, offset, limit);
            } else {
                orders = orderDao.selectUnpaidOrdersByUserId(userId, offset, limit);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return orders;
    }

    @Override
    public List<Order> selectAllOrders(int offset, int limit) throws ServiceException {
        List<Order> orders;
        try {
            orders = orderDao.selectAllOrders(offset, limit);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return markLateOrders(orders);
    }

    @Override
    public List<Order> selectAllOrdersByPayment(boolean isPayed, int offset, int limit) throws ServiceException {
        List<Order> orders;
        try {
            if (isPayed) {
                orders = orderDao.selectAllPaidOrders(offset, limit);
            } else {
                orders = orderDao.selectAllUnpaidOrders(offset, limit);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return markLateOrders(orders);
    }

    private List<Order> markLateOrders(List<Order> orders) {
        GregorianCalendar now = new GregorianCalendar();
        now.add(Calendar.DAY_OF_MONTH, -3);
        for (Order order: orders) {
            if (order.getPaymentDateTime() != null) {
                order.setLate(now.compareTo(order.getCreationDateTime()) > 0);
            }
        }
        return orders;
    }
    @Override
    public boolean submitOrder(long orderId) throws ServiceException {
        boolean result = false;
        try {
            if (orderDao.selectItemCountInOrder(orderId) > 0) {
                result = orderDao.updateOrderStatusToSubmitted(orderId);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return result;
    }

    @Override
    public boolean addItemsToOrder(long itemId, int count, long userId) throws ServiceException {
        try {
            Long currentOrderId = orderDao.selectCurrentOrderId(userId);
            if (currentOrderId == null) {
                if ((currentOrderId = orderDao.insertNewOrder(userId)) == null) {
                    return false;
                }
            }
            return orderDao.addItemsToOrder(itemId, count, currentOrderId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean removeItemFromOrder(long itemId, long userId) throws ServiceException {
        boolean result = false;
        try {
            Long currentOrderId = orderDao.selectCurrentOrderId(userId);
            if (currentOrderId != null) {
                result = orderDao.removeItemsFromOrder(itemId, currentOrderId);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return result;
    }


    @Override
    public boolean payForOrder(long orderId, String order) throws ServiceException {
        try {
            return orderDao.updateOrderPayment(orderId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean deleteOrder(long orderId) throws ServiceException {
        try {
            return orderDao.deleteOrder(orderId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Order selectOrder(long orderId) throws ServiceException {
        Map<Item, Integer> items;
        Order order;
        try {
            order = orderDao.selectOrder(orderId);
            if (order != null) {
                items = orderDao.getItemsByOrderId(orderId);
                if (items != null) {
                    order.setItems(items);
                }
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return order;
    }

    @Override
    public Order selectCurrentOrder(long userId) throws ServiceException {
        Map<Item, Integer> items;
        Order order;
        try {
            order = orderDao.selectCurrentOrder(userId);
            if (order != null) {
                items = orderDao.getItemsByOrderId(order.getId());
                if (items != null) {
                    order.setItems(items);
                }
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return order;
    }

    @Override
    public Long selectOrderOwnerId(long orderId) throws ServiceException {
        try {
            return orderDao.selectOrderOwnerId(orderId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
