package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.service.ItemService;
import by.bsuir.hockeyshop.service.impl.ItemServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code UpdateItemPriceCommand} is an admin-only implementation of {@see ActionCommand}
 * for updating price of an existing item in the catalog
 */
public class UpdateItemPriceCommand implements ActionCommand {
    private static final ItemService ITEM_SERVICE = ItemServiceImpl.getInstance();
    static final String ATTR_USER = "user";
    static final String PARAM_ID = "id";
    static final String PARAM_PRICE = "price";
    static final String ATTR_SUCCESS = "successMessage";
    static final String ATTR_ERROR = "errorMessage";
    static final String COMMAND_VIEW_ITEM = "/controller?command=view_item&id=";

    /**
     * Handles request to the servlet by trying to update price of a specified item
     * @param request request from the servlet, containing item's id and its new price
     * @return path to the items page
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        long itemId = Long.parseLong(request.getParameter(PARAM_ID));
        int price = Integer.parseInt(request.getParameter(PARAM_PRICE));
        try {
            String resultAttr = ATTR_ERROR;
            if (ITEM_SERVICE.updateItemPrice(itemId, price)) {
                resultAttr = ATTR_SUCCESS;
            }
            request.getSession().setAttribute(resultAttr, ActionResult.PRICE_UPDATED);
            page = COMMAND_VIEW_ITEM + itemId;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
