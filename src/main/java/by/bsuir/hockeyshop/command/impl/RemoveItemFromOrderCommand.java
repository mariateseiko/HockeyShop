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
 * Class {@code RemoveItemFromOrderCommand} is a client-only implementation of {@see ActionCommand}
 * for removing from the current an earlier added item
 */
public class RemoveItemFromOrderCommand implements ActionCommand {
    static final String ATTR_USER = "user";
    static final String PARAM_ITEM_ID = "itemId";
    static final String ATTR_SUCCESS = "successMessage";
    static final String ATTR_ERROR = "errorMessage";
    static final String COMMAND_VIEW_ORDER_ITEMS = "/controller?command=view_order_items&id=";
    private static final OrderService ORDER_SERVICE = OrderServiceImpl.getInstance();

    /**
     * Handles request to the servlet by trying to remove a specified item from the current order
     * @param request request from the servlet, containing item's id
     * @return path to the order items page
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        User user = (User)(request.getSession().getAttribute(ATTR_USER));
        long itemId = Long.parseLong(request.getParameter(PARAM_ITEM_ID));
        try {
            String resultAttr = ATTR_ERROR;
            if (ORDER_SERVICE.removeItemFromOrder(itemId, user.getId())) {
                resultAttr = ATTR_SUCCESS;
            }
            request.getSession().setAttribute(resultAttr, ActionResult.ITEM_REMOVED);
            page=COMMAND_VIEW_ORDER_ITEMS + itemId;
        } catch (ServiceException e) {
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
