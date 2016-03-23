package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;

import by.bsuir.hockeyshop.service.OrderService;
import by.bsuir.hockeyshop.service.ServiceException;

import by.bsuir.hockeyshop.service.impl.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code PayOrderCommand} is a client-only implementation of {@see ActionCommand}
 * for paying for an earlier submitted order
 */
public class PayOrderCommand implements ActionCommand {
    private static final OrderService ORDER_SERVICE = OrderServiceImpl.getInstance();
    static final String PARAM_CARD = "card";
    static final String PARAM_ORDER_ID = "id";
    static final String ATTR_SUCCESS = "successMessage";
    static final String ATTR_ERROR = "errorMessage";
    static final String ATTR_MESSAGE_MANAGER = "messageManager";
    static final String COMMAND_VIEW_ORDER_ITEMS = "/controller?command=view_order_items&id=";

    /**
     * Handles request to the servlet by trying to make a payment for a specified order
     * @param request request from the servlet, containing order id and payment credentials
     * @return path to the order page in case of success
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        try {
            long orderId = Long.parseLong(request.getParameter(PARAM_ORDER_ID));
            String card = request.getParameter(PARAM_CARD);
            String resultAttr = ATTR_ERROR;
            if (ORDER_SERVICE.payForOrder(orderId, card)) {
                resultAttr = ATTR_SUCCESS;
            }
            request.getSession().setAttribute(resultAttr, ActionResult.ORDER_PAID);
            page = COMMAND_VIEW_ORDER_ITEMS+orderId;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
