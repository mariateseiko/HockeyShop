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
import java.util.List;
/**
 * Class {@code ViewUserOrdersCommand} is a client-only implementation of {@see ActionCommand}
 * for retrieving and displaying his or her own orders.
 */
public class ViewUserOrdersCommand implements ActionCommand {
    private static final OrderService ORDER_SERVICE = OrderServiceImpl.getInstance();
    static final String PARAM_TYPE = "type";
    static final String PARAM_ID = "id";
    static final String PARAM_NO_ORDERS_MESSAGE = "noOrdersMessage";
    static final String PARAM_ERROR_MESSAGE = "errorMessage";
    static final String ATTR_USER = "user";
    static final String ATTR_ORDER_TYPE = "orderType";
    static final String ATTR_ORDERS = "orders";
    static final String PARAM_PAGE = "page";
    static final int DEFAULT_START_PAGE = 1;
    static final int MAX_ORDERS_PER_PAGE = 10;
    static final String ATTR_MESSAGE_MANAGER = "messageManager";

    /**
     * Handles request to the servlet by retrieving and returning a list of user's orders
     * @param request request from the servlet, containing start page number. In case of absence, the first page is retrieved.
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
            if (userParam != null) {
                userId = Long.parseLong(userParam);
            } else {
                userId = user.getId();
            }
            if (user.getRole() != UserRole.ADMIN && userId != user.getId()) {
                return ConfigurationManager.getProperty("path.page.index");
            }
            String orderType = request.getParameter(PARAM_TYPE);
            if (orderType == null) {
                orderType = OrderType.ALL.toString();
            }
            int pageNumber; String pageNumberParam;
            if ((pageNumberParam = request.getParameter(PARAM_PAGE)) != null) {
                pageNumber = Integer.parseInt(pageNumberParam)+1;
            } else {
                pageNumber = DEFAULT_START_PAGE;
            }
            switch (OrderType.valueOf(orderType.toUpperCase())) {
                case PAID:
                    orders = ORDER_SERVICE.selectOrdersByUser(userId, true,
                            (pageNumber - 1) * MAX_ORDERS_PER_PAGE, MAX_ORDERS_PER_PAGE);
                    break;
                case UNPAID:
                    orders = ORDER_SERVICE.selectOrdersByUser(userId,
                            false, (pageNumber - 1) * MAX_ORDERS_PER_PAGE, MAX_ORDERS_PER_PAGE);
                    break;
                default:
                    orders = ORDER_SERVICE.selectOrdersByUser(userId,
                            (pageNumber - 1) * MAX_ORDERS_PER_PAGE, MAX_ORDERS_PER_PAGE);
            }
            if (orders == null) {
                request.setAttribute(PARAM_NO_ORDERS_MESSAGE,
                        messageManager.getProperty("message.user.order.noorders"));
            } else {
                request.setAttribute(ATTR_ORDERS, orders);
            }
            request.setAttribute(ATTR_ORDER_TYPE, orderType);
            page = ConfigurationManager.getProperty("path.page.orders");
        } catch (ServiceException|EnumConstantNotPresentException e) {
            throw new CommandException(e);
        }
        return page;
    }

    enum OrderType {
        PAID, UNPAID, ALL
    }
}
