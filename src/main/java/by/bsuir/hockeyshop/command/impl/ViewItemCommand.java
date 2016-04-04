package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.command.MessageAdder;
import by.bsuir.hockeyshop.entity.Item;
import by.bsuir.hockeyshop.managers.ConfigurationManager;
import by.bsuir.hockeyshop.managers.MessageManager;
import by.bsuir.hockeyshop.service.ItemService;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.service.impl.ItemServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code ViewItemCommand} is an all-users implementation of {@see ActionCommand}
 * for viewing an item's info
 */
public class ViewItemCommand implements ActionCommand {
    static final String PARAM_ITEM_ID = "id";
    static final String PARAM_DIRECTION = "dir";
    static final String PARAM_LAST_PAGE = "lastPage";
    static final String ATTR_ITEM = "item";
    static final String ATTR_ERROR = "errorMessage";
    static final String ATTR_MESSAGE_MANAGER = "messageManager";
    private static final ItemService ITEM_SERVICE = ItemServiceImpl.getInstance();

    /**
     * Handles request to the servlet by retrieving a specified item's info
     * @param request request from the servlet, containing the specified item's id
     * @return path to the item's page
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page = null;
        Item item;
        long itemId = Long.parseLong(request.getParameter(PARAM_ITEM_ID));
        try {
            if ((item = ITEM_SERVICE.selectItem(itemId)) != null) {
                request.setAttribute(ATTR_ITEM, item);
                String lastPage = request.getParameter(PARAM_LAST_PAGE);
                String dir = request.getParameter(PARAM_DIRECTION);
                request.setAttribute(PARAM_LAST_PAGE, lastPage);
                request.setAttribute(PARAM_DIRECTION, dir);
                MessageAdder.addMessage(request);
                page = ConfigurationManager.getProperty("path.page.item");
            } else {
                MessageManager messageManager = (MessageManager)(request.getSession().getAttribute(ATTR_MESSAGE_MANAGER));
                request.setAttribute(ATTR_ERROR, messageManager.getProperty("message.item.not.found"));
            }
        } catch (ServiceException e) {

            throw new CommandException(e);
        }
        return page;
    }
}
