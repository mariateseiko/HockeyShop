package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
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
    static final String ATTR_SUCCESS = "successMessage";
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
            if ((item = ITEM_SERVICE.getItem(itemId)) != null) {
                request.setAttribute(ATTR_ITEM, item);
                String lastPage = request.getParameter(PARAM_LAST_PAGE);
                String dir = request.getParameter(PARAM_DIRECTION);
                request.setAttribute(PARAM_LAST_PAGE, lastPage);
                request.setAttribute(PARAM_DIRECTION, dir);
                addMessage(request);
                page = ConfigurationManager.getProperty("path.page.item");
            } else {
                    //TODO 404
            }
        } catch (ServiceException e) {
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }

    private void addMessage(HttpServletRequest request) {
        MessageManager messageManager = (MessageManager)(request.getSession().getAttribute(ATTR_MESSAGE_MANAGER));
        ActionResult success = (ActionResult) request.getSession().getAttribute(ATTR_SUCCESS);
        String message;
        if (success!= null) {
            switch (success) {
                case ITEM_ADDED_TO_ORDER:
                    message = messageManager.getProperty("message.order.add.item.success");
                    break;
                default:
                    message = "";
            }
            request.getSession().removeAttribute(ATTR_SUCCESS);
            request.setAttribute(ATTR_SUCCESS, message);

        } else {
            ActionResult error = (ActionResult) request.getSession().getAttribute(ATTR_ERROR);
            if (error != null) {
                switch (error) {
                    case ITEM_ADDED_TO_ORDER:
                        message = messageManager.getProperty("message.order.add.item.success");
                        break;
                    default:
                        message = "";
                }
                request.setAttribute(ATTR_ERROR, message);
                request.getSession().removeAttribute(ATTR_ERROR);
            }
        }
    }
}
