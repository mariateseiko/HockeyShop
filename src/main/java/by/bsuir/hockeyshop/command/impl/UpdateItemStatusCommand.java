package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.util.ActionResult;
import by.bsuir.hockeyshop.entity.ItemStatus;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.service.ItemService;
import by.bsuir.hockeyshop.service.impl.ItemServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code UpdateItemStatusCommand} is an admin-only implementation of {@see ActionCommand}
 * for updating an existing item's status
 */
public class UpdateItemStatusCommand implements ActionCommand {
    private static ItemService itemService = ItemServiceImpl.getInstance();
    private static final String PARAM_ID = "id";
    private static final String PARAM_STATUS = "status";
    private static final String ATTR_SUCCESS = "successMessage";
    private static final String ATTR_ERROR = "errorMessage";
    private static final String COMMAND_VIEW_ITEM = "/controller?command=view_item&id=";

    /**
     * Handles request to the servlet by trying to update status of a specified item
     * @param request request from the servlet, containing item's id and its new status
     * @return path to the items page
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        try {
            long itemId = Long.parseLong(request.getParameter(PARAM_ID));
            ItemStatus status = ItemStatus.valueOf(request.getParameter(PARAM_STATUS).toUpperCase());
            String resultAttr = ATTR_ERROR;
            if (itemService.updateItemStatus(itemId, status)) {
                resultAttr = ATTR_SUCCESS;
            }
            request.getSession().setAttribute(resultAttr, ActionResult.STATUS_UPDATED);
            page = COMMAND_VIEW_ITEM + itemId;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
