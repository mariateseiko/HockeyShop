package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;

import by.bsuir.hockeyshop.command.util.ActionResult;
import by.bsuir.hockeyshop.service.OrderService;
import by.bsuir.hockeyshop.service.ServiceException;

import by.bsuir.hockeyshop.service.impl.OrderServiceImpl;
import by.bsuir.hockeyshop.command.util.Validator;

import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code PayOrderCommand} is a client-only implementation of {@see ActionCommand}
 * for paying for an earlier submitted order
 */
public class PayOrderCommand implements ActionCommand {
    private static OrderService orderService = OrderServiceImpl.getInstance();

    private static final String PARAM_CARD = "card";
    private static final String PARAM_ORDER_ID = "id";
    private static final String ATTR_SUCCESS = "successMessage";
    private static final String ATTR_ERROR = "errorMessage";
    private static final String COMMAND_VIEW_ORDER_ITEMS = "/controller?command=view_order_items&id=";

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
            if (Validator.validateCard(card)) {
                String resultAttr = ATTR_ERROR;
                if (orderService.payForOrder(orderId, card)) {
                    resultAttr = ATTR_SUCCESS;
                }
                request.getSession().setAttribute(resultAttr, ActionResult.ORDER_PAID);
            } else {
                request.getSession().setAttribute(ATTR_ERROR, ActionResult.VALIDATION_FAILED);
            }
            page = COMMAND_VIEW_ORDER_ITEMS + orderId;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
