package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.entity.User;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.managers.ConfigurationManager;
import by.bsuir.hockeyshop.managers.MessageManager;
import by.bsuir.hockeyshop.service.UserService;
import by.bsuir.hockeyshop.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/**
 * Class {@code LoginCommand} is a guest-only implementation of {@see ActionCommand}
 * for signing in a user with given credentials
 */
public class LoginCommand implements ActionCommand {
    static final String PARAM_NAME_LOGIN = "login";
    static final String PARAM_NAME_PASSWORD = "password";
    static final String ATTR_USER = "user";
    static final String ATTR_ERROR = "errorMessage";
    static final String COMMAND_VIEW_PAGE = "/controller?command=view_page&page=";
    static final UserService userService = UserServiceImpl.getInstance();
    static final String ATTR_MESSAGE_MANAGER = "messageManager";

    /**
     * Handles request to the servlet by trying to log in a user with given credentials
     * @param request request from the servlet, containing user's login and password
     * @return path to index page in case of success, to login page in case of incorrect login or password
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        String login = request.getParameter(PARAM_NAME_LOGIN);
        String pass = request.getParameter(PARAM_NAME_PASSWORD);
        User user;
        MessageManager messageManager = (MessageManager)(request.getSession().getAttribute(ATTR_MESSAGE_MANAGER));
        try {
            if ((user = userService.loginUser(login, pass) )!= null) {
                HttpSession session = request.getSession();
                session.setAttribute(ATTR_USER, user);
                page = ConfigurationManager.getProperty("path.page.index");
            } else {
                request.getSession().setAttribute(ATTR_ERROR, ActionResult.LOGIN);
                page = COMMAND_VIEW_PAGE + ConfigurationManager.getProperty("path.page.login");
            }
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
