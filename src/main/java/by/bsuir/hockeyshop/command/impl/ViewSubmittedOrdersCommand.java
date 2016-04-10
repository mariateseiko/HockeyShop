package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.command.util.MessageAdder;
import by.bsuir.hockeyshop.entity.Order;
import by.bsuir.hockeyshop.entity.OrderType;
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
    private static OrderService orderService = OrderServiceImpl.getInstance();

    private static final String PARAM_TYPE = "type";
    private static final String PARAM_NO_ORDERS_MESSAGE = "noOrdersMessage";
    private static final String ATTR_MESSAGE_MANAGER = "messageManager";
    private static final String ATTR_ORDERS = "orders";

    /**
     * Handles request to the servlet by retrieving and returning a list of submitted orders by type.
     * If order type is not specified, orders af all types are displayed.
     * @param request request from the servlet, containing an order type (paid, unpaid, all)
     * @return path to the orders' page
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        List<Order> orders = null;
        MessageManager messageManager = (MessageManager)(request.getSession().getAttribute(ATTR_MESSAGE_MANAGER));
        try {
            String orderType = request.getParameter(PARAM_TYPE);
            if (orderType == null || orderType.isEmpty()) {
                orderType = OrderType.ALL.toString();
            }
            switch (OrderType.valueOf(orderType.toUpperCase())) {
                case PAID:
                    orders = orderService.selectAllOrdersByPayment(true);
                    break;
                case UNPAID:
                    orders = orderService.selectAllOrdersByPayment(false);
                    break;
                case ALL:
                    orders = orderService.selectAllOrders();
                    break;
            }
            if (orders != null) {
                request.setAttribute(ATTR_ORDERS, orders);
                MessageAdder.addMessage(request);
            } else {
                request.setAttribute(PARAM_NO_ORDERS_MESSAGE,
                        messageManager.getProperty("message.user.order.noorders"));
            }
            page = ConfigurationManager.getProperty("path.page.orders");
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
