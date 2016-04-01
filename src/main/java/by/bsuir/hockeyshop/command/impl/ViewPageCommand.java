package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.managers.MessageManager;

import javax.servlet.http.HttpServletRequest;

public class ViewPageCommand implements ActionCommand {
    private static final String PARAM_PAGE_NAME = "page";
    private static final String ATTR_SUCCESS = "successMessage";
    private static final String ATTR_ERROR = "errorMessage";
    static final String ATTR_MESSAGE_MANAGER = "messageManager";

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        addMessage(request);
        return request.getParameter(PARAM_PAGE_NAME);
    }

    private void addMessage(HttpServletRequest request) {
        MessageManager messageManager = (MessageManager)(request.getSession().getAttribute(ATTR_MESSAGE_MANAGER));
        ActionResult success = (ActionResult) request.getSession().getAttribute(ATTR_SUCCESS);
        String message = "";
        if (success!= null) {
            switch (success) {
                case REGISTER:
                    message = messageManager.getProperty("message.register.success");
                    break;
                case ITEM_ADDED_TO_CATALOG:
                    message = messageManager.getProperty("message.item.add.success");
            }
            request.getSession().removeAttribute(ATTR_SUCCESS);
            request.setAttribute(ATTR_SUCCESS, message);

        } else {
            ActionResult error = (ActionResult) request.getSession().getAttribute(ATTR_ERROR);
            if (error != null) {
                switch (error) {
                    case LOGIN:
                        message = messageManager.getProperty("message.login.error");
                        break;
                    case REGISTER:
                        message = messageManager.getProperty("message.register.error");
                        break;
                    case ITEM_ADDED_TO_CATALOG:
                        message = messageManager.getProperty("message.item.add.error");
                        break;
                    case VALIDATION_FAILED:
                        message = messageManager.getProperty("message.item.add.validation.error");
                }
            }
            request.setAttribute(ATTR_ERROR, message);
            request.getSession().removeAttribute(ATTR_ERROR);
        }
    }
}
