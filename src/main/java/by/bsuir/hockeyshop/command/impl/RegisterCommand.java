package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.command.util.ActionResult;
import by.bsuir.hockeyshop.entity.User;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.managers.ConfigurationManager;
import by.bsuir.hockeyshop.service.UserService;
import by.bsuir.hockeyshop.service.impl.UserServiceImpl;
import by.bsuir.hockeyshop.command.util.Validator;

import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code RegisterCommand} is a guest-only implementation of {@see ActionCommand}
 * for registration of a new user
 */
public class RegisterCommand implements ActionCommand {
    private static final String PARAM_NAME_LOGIN = "login";
    private static final String PARAM_NAME_PASSWORD = "password";
    private static final String PARAM_EMAIL = "email";
    private static final String ATTR_ERROR = "errorMessage";
    private static final String ATTR_SUCCESS = "successMessage";
    private static final String COMMAND_VIEW_PAGE = "/controller?command=view_page&page=";

    private static UserService userService = UserServiceImpl.getInstance();

    /**
     * Handles request to the servlet by trying to register a new user
     * @param request request from the servlet, containing user's login, password and email
     * @return path to the login page in case of success or to the registration page in case of invalid input
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        String login = request.getParameter(PARAM_NAME_LOGIN);
        String pass = request.getParameter(PARAM_NAME_PASSWORD);
        String email = request.getParameter(PARAM_EMAIL);
        User user = new User(login, pass, email);
        if (Validator.validateUser(user)) {
            try {
                if (userService.registerUser(login, pass, email)) {
                    request.getSession().setAttribute(ATTR_SUCCESS, ActionResult.REGISTER);
                    page = COMMAND_VIEW_PAGE + ConfigurationManager.getProperty("path.page.login");

                } else {
                    request.getSession().setAttribute(ATTR_ERROR, ActionResult.REGISTER);
                    page = COMMAND_VIEW_PAGE + ConfigurationManager.getProperty("path.page.register");
                }
            } catch (ServiceException e) {
                throw new CommandException(e);
            }
        } else {
            request.getSession().setAttribute(ATTR_ERROR, ActionResult.REGISTER_VALIDATED);
            page = COMMAND_VIEW_PAGE + ConfigurationManager.getProperty("path.page.register");
        }
        return page;
    }
}
