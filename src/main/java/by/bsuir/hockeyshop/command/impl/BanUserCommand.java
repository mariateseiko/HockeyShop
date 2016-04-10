package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.command.util.ActionResult;
import by.bsuir.hockeyshop.entity.User;
import by.bsuir.hockeyshop.listener.SessionListener;
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
    private static final String PARAM_USER_ID = "id";
    private static final String COMMAND_VIEW_USER = "/controller?command=view_user&id=";
    private static final String ATTR_SUCCESS = "successMessage";
    private static final String ATTR_ERROR = "errorMessage";
    private static final String ATTR_BAN = "ban";

    private static UserService userService = UserServiceImpl.getInstance();

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
        try {
            if (userService.changeUserBanStatus(id, ban)) {
                if (ban) {
                    request.getSession().setAttribute(ATTR_SUCCESS, ActionResult.USER_BANNED);
                    Map<String, HttpSession> sessions = SessionListener.getSessionMap();
                    for(HttpSession session: sessions.values()) {
                        User user = (User)session.getAttribute("user");
                        if (user != null && user.getId() == id) {
                            session.invalidate();
                            break;
                        }
                    }
                } else {
                    request.getSession().setAttribute(ATTR_SUCCESS, ActionResult.USER_UNBANNED);
                }
            } else {
                if (ban) {
                    request.getSession().setAttribute(ATTR_ERROR, ActionResult.USER_BANNED);
                } else {
                    request.getSession().setAttribute(ATTR_ERROR, ActionResult.USER_UNBANNED);
                }
            }
            page = COMMAND_VIEW_USER + id;
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return page;
    }
}
