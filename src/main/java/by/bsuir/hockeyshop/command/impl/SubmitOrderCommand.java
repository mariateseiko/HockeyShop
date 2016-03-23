package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.service.OrderService;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.service.impl.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code SubmitOrderCommand} is a client-only implementation of {@see ActionCommand}
 * for submitting the current order
 */
public class SubmitOrderCommand implements ActionCommand {
    static final String PARAM_ORDER_ID = "id";
    static final String ATTR_SUCCESS = "successSubmit";
    static final String ATTR_ERROR = "errorMessage";
    static final String ATTR_USER = "user";
    static final String ATTR_MESSAGE_MANAGER = "messageManager";
    static final String COMMAND_VIEW_ORDER_ITEMS = "/controller?command=view_order_items&id=";
    private static final OrderService orderService = OrderServiceImpl.getInstance();

    /**
     * Handles request to the servlet by trying to submit user's current order
     * @param request request from the servlet, containing order's id
     * @return path to the order items page
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        long orderId = Long.parseLong(request.getParameter(PARAM_ORDER_ID));
        try {
            String resultAttr = ATTR_ERROR;
            if (orderService.submitOrder(orderId)) {
               resultAttr = ATTR_SUCCESS;
            }
            request.getSession().setAttribute(resultAttr, ActionResult.ORDER_SUBMITTED);
            page = COMMAND_VIEW_ORDER_ITEMS+orderId;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
