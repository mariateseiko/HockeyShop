package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.managers.ConfigurationManager;
import by.bsuir.hockeyshop.managers.MessageManager;
import by.bsuir.hockeyshop.service.OrderService;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.service.impl.OrderServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code DeleteOrderCommand} is a guest-only implementation of {@see ActionCommand}
 * for deleting an earlier submitted order
 */
public class DeleteOrderCommand implements ActionCommand {
    static final String ATTR_USER = "user";
    static final String PARAM_ORDER_ID = "id";
    static final String ATTR_SUCCESS = "successMessage";
    static final String ATTR_ERROR = "errorMessage";
    static final String COMMAND_VIEW_ORDERS = "/controller?command=view_user_orders";
    static final String ATTR_MESSAGE_MANAGER = "messageManager";
    private static final OrderService ORDER_SERVICE = OrderServiceImpl.getInstance();

    /**
     * Handles request to the servlet by  deleting the specified order
     * @param request request from the servlet, containing the id of order to delete
     * @return path to all orders jsp page
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        long orderId = Long.parseLong(request.getParameter(PARAM_ORDER_ID));
        try {
            String resultAttr = ATTR_ERROR;
            if (ORDER_SERVICE.deleteOrder(orderId)) {
                resultAttr = ATTR_SUCCESS;
            }
            request.getSession().setAttribute(resultAttr, ActionResult.ORDER_DELETED);
            page=COMMAND_VIEW_ORDERS;
        } catch (ServiceException e) {
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
