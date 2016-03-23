package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.entity.User;
import by.bsuir.hockeyshop.managers.ConfigurationManager;
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
                //TODO 404
            }
            page = ConfigurationManager.getProperty("path.page.user");
        } catch (ServiceException e) {
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}