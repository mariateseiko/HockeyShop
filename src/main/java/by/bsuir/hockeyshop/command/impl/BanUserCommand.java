package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.command.CommandStorage;
import by.bsuir.hockeyshop.entity.User;
import by.bsuir.hockeyshop.listener.SessionListener;
import by.bsuir.hockeyshop.managers.MessageManager;
import by.bsuir.hockeyshop.service.ServiceException;
import by.bsuir.hockeyshop.service.UserService;
import by.bsuir.hockeyshop.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Class {@code BanUserCommand} is an admin-only implementation of {@see ActionCommand}
 * for banning or unbanning users
 */
public class BanUserCommand implements ActionCommand {
    static final String ATTR_USER = "user";
    static final String PARAM_USER_ID = "id";
    static final String PARAM_LOGIN_ERROR_MESSAGE = "loginErrorMessage";
    static final UserService userService = UserServiceImpl.getInstance();
    static final String ATTR_MESSAGE_MANAGER = "messageManager";
    static final String VIEW_USER_COMMAND = "view_user";
    static final String ATTR_SUCCESS = "successMessage";
    static final String ATTR_ERROR = "errorMessage";
    static final String ATTR_BAN = "ban";

    /**
     * Handles request to the servlet by banning or unbanning a user with specified id
     * @param request request from the servlet, containing id of the user and boolean parameter ban, specifying whether
     *                the user should be banned({@code true}) or unbanned ({@code false})
     * @return path to user info jsp page
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page;
        Long id = Long.parseLong(request.getParameter(PARAM_USER_ID));
        Boolean ban = Boolean.parseBoolean(request.getParameter(ATTR_BAN));
        MessageManager messageManager = (MessageManager)(request.getSession().getAttribute(ATTR_MESSAGE_MANAGER));
        try {
            if (userService.changeUserBanStatus(id, ban)) {
                if (ban) {
                    request.setAttribute(ATTR_SUCCESS, messageManager.getProperty("message.ban.success"));
                    Map<String, HttpSession> sessions = SessionListener.getSessionMap();
                    for(HttpSession session: sessions.values()) {
                        User user = (User)session.getAttribute("user");
                        if (user != null && user.getId() == id) {
                            session.invalidate();
                            break;
                        }
                    }
                } else {
                    request.setAttribute(ATTR_SUCCESS, messageManager.getProperty("message.unban.success"));
                }
                request.setAttribute(PARAM_LOGIN_ERROR_MESSAGE, messageManager.getProperty("message.login.error"));
            } else {
                if (ban) {
                    request.setAttribute(ATTR_ERROR, messageManager.getProperty("message.ban.error"));
                } else {
                    request.setAttribute(ATTR_ERROR, messageManager.getProperty("message.unban.error"));
                }
            }
            page = CommandStorage.getInstance().getCommand(VIEW_USER_COMMAND).execute(request);
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
