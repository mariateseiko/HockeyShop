package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
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
    private static final OrderService ORDER_SERVICE = OrderServiceImpl.getInstance();
    static final String PARAM_NO_ITEMS_MESSAGE = "noOrdersMessage";
    static final String ATTR_ERROR = "errorMessage";
    static final String ATTR_SUCCESS = "successMessage";
    static final String PARAM_ORDER_ID = "id";
    static final String ATTR_USER = "user";
    static final String ATTR_ITEMS = "items";
    static final String ATTR_QUANTITY = "quantity";
    static final String ATTR_TOTAL_SUM = "sum";
    static final String ATTR_CREATION_DATE = "creationDate";
    static final String ATTR_PAYMENT_DATE = "paymentDate";
    static final String ATTR_MESSAGE_MANAGER = "messageManager";
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
                if (user.getRole() != UserRole.ADMIN && user.getId() != ORDER_SERVICE.selectOrderOwnerId(orderId)) {
                    return ConfigurationManager.getProperty("path.page.index");
                }
                order = ORDER_SERVICE.selectOrder(orderId);
            } else {
                order = ORDER_SERVICE.selectCurrentOrder(user.getId());
            }
            if (order != null) {
                request.setAttribute(PARAM_ORDER_ID, order.getId());
                request.setAttribute(ATTR_QUANTITY, order.getCountOfItems());
                request.setAttribute(ATTR_TOTAL_SUM, order.getPaymentSum());
                request.setAttribute(ATTR_CREATION_DATE, order.getCreationDateTime());
                request.setAttribute(ATTR_PAYMENT_DATE, order.getPaymentDateTime());
                if (order.getItems() != null) {
                    request.setAttribute(ATTR_ITEMS, order.getItems());
                } else {
                    request.setAttribute(PARAM_NO_ITEMS_MESSAGE,
                            messageManager.getProperty("message.order.noitems"));
                }
                addMessage(request);
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

    private void addMessage(HttpServletRequest request) {
        MessageManager messageManager = (MessageManager)(request.getSession().getAttribute(ATTR_MESSAGE_MANAGER));
        ActionResult success = (ActionResult) request.getSession().getAttribute(ATTR_SUCCESS);
        String message = "";
        if (success!= null) {
            switch (success) {
                case ORDER_PAID:
                    message = messageManager.getProperty("message.order.pay.success");
                    break;
                case ORDER_SUBMITTED:
                    message = messageManager.getProperty("message.order.submit.success");
                    break;
                case ITEM_REMOVED:
                    message = messageManager.getProperty("message.order.remove.item.success");
                    break;
            }
            request.getSession().removeAttribute(ATTR_SUCCESS);
            request.setAttribute(ATTR_SUCCESS, message);

        } else {
            ActionResult error = (ActionResult) request.getSession().getAttribute(ATTR_ERROR);
            if (error != null) {
                switch (error) {
                    case ORDER_PAID:
                        message = messageManager.getProperty("message.order.pay.error");
                        break;
                    case ORDER_SUBMITTED:
                        message = messageManager.getProperty("message.order.submit.error");
                        break;
                    case ITEM_REMOVED:
                        message = messageManager.getProperty("message.order.remove.item.error");
                        break;
                }
            }
            request.setAttribute(ATTR_ERROR, message);
            request.getSession().removeAttribute(ATTR_ERROR);
        }
    }
}
