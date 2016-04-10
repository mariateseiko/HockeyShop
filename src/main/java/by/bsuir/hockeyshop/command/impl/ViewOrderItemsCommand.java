package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.command.util.MessageAdder;
import by.bsuir.hockeyshop.entity.Order;
import by.bsuir.hockeyshop.entity.User;
import by.bsuir.hockeyshop.entity.UserRole;
import by.bsuir.hockeyshop.managers.ConfigurationManager;
import by.bsuir.hockeyshop.managers.MessageManager;
import by.bsuir.hockeyshop.service.OrderService;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.service.impl.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code ViewOrderItemsCommand} is a client and admin implementation of {@see ActionCommand}
 * for viewing items belonging to a specified order
 */
public class ViewOrderItemsCommand implements ActionCommand {
    private static OrderService orderService = OrderServiceImpl.getInstance();

    private static final String PARAM_NO_ITEMS_MESSAGE = "noOrdersMessage";
    private static final String PARAM_ORDER_ID = "id";
    private static final String ATTR_USER = "user";
    private static final String ATTR_ITEMS = "items";
    private static final String ATTR_QUANTITY = "quantity";
    private static final String ATTR_TOTAL_SUM = "sum";
    private static final String ATTR_CREATION_DATE = "creationDate";
    private static final String ATTR_PAYMENT_DATE = "paymentDate";
    private static final String ATTR_MESSAGE_MANAGER = "messageManager";
    private static final String ATTR_LATE = "late";

    /**
     * Handles request to the servlet by retrieving and returning a list of order's items. If order's id is not specified,
     * current order's items are retrieved.
     * @param request request from the servlet, containing an order's id
     * @return path to the order's page
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        Order order;
        MessageManager messageManager = (MessageManager)(request.getSession().getAttribute(ATTR_MESSAGE_MANAGER));
        try {
            User user = (User)(request.getSession().getAttribute(ATTR_USER));
            String orderIdParam = request.getParameter(PARAM_ORDER_ID);
            if (orderIdParam != null) {
                Long orderId = Long.parseLong(orderIdParam);
                if (user.getRole() != UserRole.ADMIN && user.getId() != orderService.selectOrderOwnerId(orderId)) {
                    return ConfigurationManager.getProperty("path.page.index");
                }
                order = orderService.selectOrder(orderId);
            } else {
                order = orderService.selectCurrentOrder(user.getId());
            }
            if (order != null) {
                request.setAttribute(PARAM_ORDER_ID, order.getId());
                request.setAttribute(ATTR_QUANTITY, order.getCountOfItems());
                request.setAttribute(ATTR_TOTAL_SUM, order.getPaymentSum());
                request.setAttribute(ATTR_CREATION_DATE, order.getCreationDateTime());
                request.setAttribute(ATTR_PAYMENT_DATE, order.getPaymentDateTime());
                request.setAttribute(ATTR_LATE, order.isLate());
                if (order.getItems() != null) {
                    request.setAttribute(ATTR_ITEMS, order.getItems());
                } else {
                    request.setAttribute(PARAM_NO_ITEMS_MESSAGE,
                            messageManager.getProperty("message.order.noitems"));
                }
                MessageAdder.addMessage(request);
            } else {
                request.setAttribute(PARAM_NO_ITEMS_MESSAGE,
                        messageManager.getProperty("message.order.noitems"));
            }
            page = ConfigurationManager.getProperty("path.page.orderitems");
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
