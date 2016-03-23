package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.entity.Order;
import by.bsuir.hockeyshop.managers.ConfigurationManager;
import by.bsuir.hockeyshop.managers.MessageManager;
import by.bsuir.hockeyshop.service.OrderService;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.service.impl.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * Class {@code ViewSubmittedOrdersCommand} is an admin-only implementation of {@see ActionCommand}
 * for viewing different types of submitted orders
 */
public class ViewSubmittedOrdersCommand implements ActionCommand {
    private static final OrderService ORDER_SERVICE = OrderServiceImpl.getInstance();
    static final String PARAM_TYPE = "type";
    static final String PARAM_NO_ORDERS_MESSAGE = "noOrdersMessage";
    static final String PARAM_ERROR_MESSAGE = "errorMessage";
    static final String PARAM_PAGE = "page";
    static final int DEFAULT_START_PAGE = 1;
    static final int MAX_ORDERS_PER_PAGE = 10;
    static final String ATTR_MESSAGE_MANAGER = "messageManager";
    static final String ATTR_ORDERS = "orders";

    /**
     * Handles request to the servlet by retrieving and returning a list of submitted orders by type.
     * Max list length is 10. If order type is not specified, orders af all types are displayed. The first page
     * is displayed in case of page number absence in the request.
     * @param request request from the servlet, containing an order type (paid, unpaid, all) and a start page.
     * @return path to the orders' page
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        List<Order> orders;
        MessageManager messageManager = (MessageManager)(request.getSession().getAttribute(ATTR_MESSAGE_MANAGER));
        try {
            String orderType = request.getParameter(PARAM_TYPE);
            int pageNumber; String pageNumberParam;
            if ((pageNumberParam = request.getParameter(PARAM_PAGE)) != null) {
                pageNumber = Integer.parseInt(pageNumberParam)+1;
            } else {
                pageNumber = DEFAULT_START_PAGE;
            }
            if (orderType == null || orderType.isEmpty()) {
                orderType = OrderType.ALL.toString();
            }
            switch (OrderType.valueOf(orderType.toUpperCase())) {
                case PAID:
                    orders = ORDER_SERVICE.getAllOrdersByPayment(true,
                            (pageNumber-1)*MAX_ORDERS_PER_PAGE, MAX_ORDERS_PER_PAGE);
                    break;
                case UNPAID:
                    orders = ORDER_SERVICE.getAllOrdersByPayment(false,
                            (pageNumber-1)*MAX_ORDERS_PER_PAGE, MAX_ORDERS_PER_PAGE);
                    break;
                case ALL:
                    orders = ORDER_SERVICE.getAllOrders((pageNumber-1)*MAX_ORDERS_PER_PAGE, MAX_ORDERS_PER_PAGE);
                    break;
                default:
                    throw new EnumConstantNotPresentException(OrderType.class, orderType);
            }
            if (orders == null) {
                request.setAttribute(PARAM_NO_ORDERS_MESSAGE,
                        messageManager.getProperty("message.user.order.noorders"));
            } else {
                request.setAttribute(ATTR_ORDERS, orders);
            }
            page = ConfigurationManager.getProperty("path.page.orders");
        } catch (ServiceException |EnumConstantNotPresentException e) {
            page = ConfigurationManager.getProperty("path.page.error");
            request.setAttribute(PARAM_ERROR_MESSAGE,
                    messageManager.getProperty("message.error.service") + e.getCause().getCause());
        }
        return page;
    }

    enum OrderType {
        PAID, UNPAID, ALL
    }
}
