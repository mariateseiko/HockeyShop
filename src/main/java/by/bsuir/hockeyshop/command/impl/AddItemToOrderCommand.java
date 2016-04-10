package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.command.util.ActionResult;
import by.bsuir.hockeyshop.entity.User;

import by.bsuir.hockeyshop.service.OrderService;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.service.impl.OrderServiceImpl;
import by.bsuir.hockeyshop.command.util.Validator;

import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code AddItemToOrderCommand} is a client-only implementation of {@see ActionCommand}
 * for adding a chosen item to current order.
 */
public class AddItemToOrderCommand implements ActionCommand {
    private static final String ATTR_USER = "user";
    private static final String PARAM_ITEM_ID = "id";
    private static final String PARAM_ITEM_COUNT = "count";
    private static final String COMMAND_VIEW_ITEM = "/controller?command=view_item&id=";
    private static final String ATTR_SUCCESS = "successMessage";
    private static final String ATTR_ERROR = "errorMessage";

    private static OrderService orderService = OrderServiceImpl.getInstance();

    /**
     * Handles request to the servlet by adding a specified item of specified quantity to the current order.
     * @param request request from the servlet, containing id of the item to be added and it's quantity
     * @return query for viewing the item
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        User user = (User)(request.getSession().getAttribute(ATTR_USER));
        long itemId = Long.parseLong(request.getParameter(PARAM_ITEM_ID));
        String quantity = request.getParameter(PARAM_ITEM_COUNT);
        try {
            if (Validator.validateQuantity(quantity)) {
                int count = Integer.parseInt(quantity);
                String resultAttr = ATTR_ERROR;
                if (orderService.addItemsToOrder(itemId, count, user.getId())) {
                   resultAttr = ATTR_SUCCESS;
                }
                request.getSession().setAttribute(resultAttr, ActionResult.ITEM_ADDED_TO_ORDER);
            } else {
                request.getSession().setAttribute(ATTR_ERROR, ActionResult.VALIDATION_FAILED);
            }
            page= COMMAND_VIEW_ITEM + itemId;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
