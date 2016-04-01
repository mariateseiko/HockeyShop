package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.entity.User;
import by.bsuir.hockeyshop.managers.ConfigurationManager;
import by.bsuir.hockeyshop.managers.MessageManager;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.service.UserService;
import by.bsuir.hockeyshop.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code ViewUserCommand} is an admin-only implementation of {@see ActionCommand}
 * for retrieving and displaying user's info
 */
public class ViewUserCommand implements ActionCommand {
    static final String PARAM_USER_ID = "id";
    static final String ATTR_USER = "user";
    static final String ATTR_ERROR = "errorMessage";
    static final String ATTR_MESSAGE_MANAGER = "messageManager";
    private static final UserService USER_SERVICE = UserServiceImpl.getInstance();

    /**
     * Handles request to the servlet by retrieving and returning a specified user's information
     * @param request request from the servlet, containing a user's id
     * @return path to the user's page
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        User user;
        long userId = Long.parseLong(request.getParameter(PARAM_USER_ID));
        try {
            if ((user = USER_SERVICE.selectUser(userId)) != null) {
                request.setAttribute(ATTR_USER, user);
            } else {
                MessageManager messageManager = (MessageManager)(request.getSession().getAttribute(ATTR_MESSAGE_MANAGER));
                request.setAttribute(ATTR_ERROR, messageManager.getProperty("message.user.not.found"));
            }
            page = ConfigurationManager.getProperty("path.page.user");
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
