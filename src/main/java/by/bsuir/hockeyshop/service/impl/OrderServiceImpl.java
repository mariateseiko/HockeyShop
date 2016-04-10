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

/**
 * {@inheritDoc}
 *
 * A singleton implementation of the {@link OrderService} interface, using {@link OrderDaoImpl} as an underlying level
 */
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
    public List<Order> selectOrdersByUser(long userId) throws ServiceException {
        try {
            return markLateOrders(orderDao.selectOrdersByUserId(userId));
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> selectOrdersByUser(long userId, boolean paid) throws ServiceException {
        List<Order> orders;
        try {
            if (paid) {
                orders = orderDao.selectPaidOrdersByUserId(userId);
            } else {
                orders = orderDao.selectUnpaidOrdersByUserId(userId);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return markLateOrders(orders);
    }

    @Override
    public List<Order> selectAllOrders() throws ServiceException {
        List<Order> orders;
        try {
            orders = orderDao.selectAllOrders();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return markLateOrders(orders);
    }

    @Override
    public List<Order> selectAllOrdersByPayment(boolean isPayed) throws ServiceException {
        List<Order> orders;
        try {
            if (isPayed) {
                orders = orderDao.selectAllPaidOrders();
            } else {
                orders = orderDao.selectAllUnpaidOrders();
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return markLateOrders(orders);
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
            if (currentOrderId == -1) {
                if ((currentOrderId = orderDao.insertNewOrder(userId)) == null) {
                    return false;
                }
            }
            return orderDao.insertItemsToOrder(itemId, count, currentOrderId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean removeItemFromOrder(long itemId, long userId) throws ServiceException {
        boolean result = false;
        try {
            Long currentOrderId = orderDao.selectCurrentOrderId(userId);
            if (currentOrderId != -1) {
                result = orderDao.deleteItemsFromOrder(itemId, currentOrderId);
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
    public boolean deleteOrder(long orderId, long userId) throws ServiceException {
        boolean result = false;
        try {
            if (orderDao.selectOrderOwnerId(orderId) != userId) {
                result = orderDao.deleteOrder(orderId);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return result;
    }

    @Override
    public Order selectOrder(long orderId) throws ServiceException {
        Map<Item, Integer> items;
        Order order;
        try {
            order = orderDao.selectOrder(orderId);
            if (order != null) {
                items = orderDao.selectItemsByOrderId(orderId);
                if (items != null) {
                    order.setItems(items);
                }
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return markLateOrder(order);
    }

    @Override
    public Order selectCurrentOrder(long userId) throws ServiceException {
        Map<Item, Integer> items;
        Order order;
        try {
            order = orderDao.selectCurrentOrder(userId);
            if (order != null) {
                items = orderDao.selectItemsByOrderId(order.getId());
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
    public long selectOrderOwnerId(long orderId) throws ServiceException {
        try {
            return orderDao.selectOrderOwnerId(orderId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean deleteLateOrder(long orderId) throws ServiceException {
        try {
            return orderDao.deleteLateOrder(orderId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private List<Order> markLateOrders(List<Order> orders) {
        GregorianCalendar now = new GregorianCalendar();
        now.add(Calendar.DAY_OF_MONTH, -3);
        for (Order order: orders) {
            if (order.getPaymentDateTime() == null) {
                order.setLate(now.compareTo(order.getCreationDateTime()) > 0);
            }
        }
        return orders;
    }

    private Order markLateOrder(Order order) {
        GregorianCalendar now = new GregorianCalendar();
        now.add(Calendar.DAY_OF_MONTH, -3);
        if (order.getPaymentDateTime() == null) {
            order.setLate(now.compareTo(order.getCreationDateTime()) > 0);
        }
        return order;
    }
}
