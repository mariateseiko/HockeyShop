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
import java.util.List;
/**
 * Class {@code ViewUsersListCommand} is an admin-only implementation of {@see ActionCommand}
 * for retrieving and displaying list of all users
 */
public class ViewUsersListCommand implements ActionCommand {
    private static final String ATTR_USERS = "users";
    private static final String ATTR_ERROR_MESSAGE = "errorMessage";
    private static final String ATTR_MESSAGE_MANAGER = "messageManager";
    private static UserService userService = UserServiceImpl.getInstance();

    /**
     * Handles request to the servlet by retrieving a list of all users
     * @param request request from the servlet, containing start page number. In case of absence, the first page is retrieved.
     * @return path to the users' list page
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        List<User> users;
        MessageManager messageManager = (MessageManager)(request.getSession().getAttribute(ATTR_MESSAGE_MANAGER));
        try {
            users = userService.selectUsers();
            if (users == null) {
                request.setAttribute(ATTR_ERROR_MESSAGE,
                        messageManager.getProperty("message.users.nousers"));
            } else {
                request.setAttribute(ATTR_USERS, users);
            }
            page = ConfigurationManager.getProperty("path.page.users");
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
