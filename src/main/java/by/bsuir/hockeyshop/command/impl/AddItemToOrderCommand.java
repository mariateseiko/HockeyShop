package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.entity.User;
import by.bsuir.hockeyshop.managers.ConfigurationManager;

import by.bsuir.hockeyshop.service.OrderService;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.service.impl.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code AddItemToOrderCommand} is a client-only implementation of {@see ActionCommand}
 * for adding a chosen item to current order.
 */
public class AddItemToOrderCommand implements ActionCommand {
    static final String ATTR_USER = "user";
    static final String PARAM_ITEM_ID = "id";
    static final String PARAM_ITEM_COUNT = "count";
    static final String COMMAND_VIEW_ITEM = "/controller?command=view_item&id=";
    static final String ATTR_MESSAGE_MANAGER = "messageManager";
    static final String ATTR_SUCCESS = "successMessage";
    static final String ATTR_ERROR = "errorMessage";
    private static final OrderService ORDER_SERVICE = OrderServiceImpl.getInstance();

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
        int count = Integer.parseInt(request.getParameter(PARAM_ITEM_COUNT));
        try {
            String resultAttr = ATTR_ERROR;
            if (ORDER_SERVICE.addItemsToOrder(itemId, count, user.getId())) {
               resultAttr = ATTR_SUCCESS;
            }
            request.getSession().setAttribute(resultAttr, ActionResult.ITEM_ADDED_TO_ORDER);
            page=COMMAND_VIEW_ITEM + itemId;
        } catch (ServiceException e) {
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
