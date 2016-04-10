package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.command.util.MessageAdder;
import by.bsuir.hockeyshop.entity.Order;
import by.bsuir.hockeyshop.entity.OrderType;
import by.bsuir.hockeyshop.entity.User;
import by.bsuir.hockeyshop.entity.UserRole;
import by.bsuir.hockeyshop.managers.ConfigurationManager;
import by.bsuir.hockeyshop.managers.MessageManager;
import by.bsuir.hockeyshop.service.OrderService;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.service.impl.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * Class {@code ViewUserOrdersCommand} is a client and admin implementation of {@see ActionCommand}
 * for retrieving the user's (own) orders
 */
public class ViewUserOrdersCommand implements ActionCommand {
    private static OrderService orderService = OrderServiceImpl.getInstance();

    private static final String PARAM_TYPE = "type";
    private static final String PARAM_ID = "id";
    private static final String PARAM_NO_ORDERS_MESSAGE = "noOrdersMessage";
    private static final String ATTR_USER = "user";
    private static final String ATTR_ORDER_TYPE = "orderType";
    private static final String ATTR_ORDERS = "orders";
    private static final String ATTR_MESSAGE_MANAGER = "messageManager";

    /**
     * Handles request to the servlet by retrieving and returning a list of user's orders
     * @param request request from the servlet
     * @return path to the orders' page
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        List<Order> orders;
        long userId;
        User user;
        MessageManager messageManager = (MessageManager)(request.getSession().getAttribute(ATTR_MESSAGE_MANAGER));
        try {
            String userParam = request.getParameter(PARAM_ID);
            user = (User) (request.getSession().getAttribute(ATTR_USER));
            if (userParam != null && !userParam.isEmpty()) {
                userId = Long.parseLong(userParam);
                if (user.getRole() != UserRole.ADMIN && userId != user.getId()) {
                    return ConfigurationManager.getProperty("path.page.index");
                }
            } else {
                userId = user.getId();
            }
            String orderType = request.getParameter(PARAM_TYPE);
            if (orderType == null) {
                orderType = OrderType.ALL.toString();
            }
            switch (OrderType.valueOf(orderType.toUpperCase())) {
                case PAID:
                    orders = orderService.selectOrdersByUser(userId, true
                    );
                    break;
                case UNPAID:
                    orders = orderService.selectOrdersByUser(userId,
                            false);
                    break;
                default:
                    orders = orderService.selectOrdersByUser(userId
                    );
            }
            if (orders != null) {
                request.setAttribute(ATTR_ORDERS, orders);
                MessageAdder.addMessage(request);
            } else {
                request.setAttribute(PARAM_NO_ORDERS_MESSAGE,
                        messageManager.getProperty("message.user.order.noorders"));
            }
            request.setAttribute(ATTR_ORDER_TYPE, orderType);
            page = ConfigurationManager.getProperty("path.page.orders");
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
