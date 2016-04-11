package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.command.util.AllowedPage;
import by.bsuir.hockeyshop.command.util.MessageAdder;
import by.bsuir.hockeyshop.entity.User;
import by.bsuir.hockeyshop.entity.UserRole;
import by.bsuir.hockeyshop.managers.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;

/**
 * Extracts all present messages from the session to the request, checks security rights
 * and defines the page path to forward to
 */
public class ViewPageCommand implements ActionCommand {
    private static final String PARAM_PAGE_NAME = "page";
    private static final String ATTR_USER = "user";

    /**
     * Checks whether page is allowed to be viewed by a user. If allowed, adds messages to the request
     * and returns a page to view. If not, return path to the index page
     * @param request request from the servlet which may contain messages in the session
     * @return path to the page to view
     */
    @Override
    public String execute(HttpServletRequest request) {
        String result = ConfigurationManager.getProperty("path.page.index");
        String requestedPage = request.getParameter(PARAM_PAGE_NAME).toUpperCase();
        User user = (User)(request.getSession().getAttribute(ATTR_USER));
        if (user == null) {
            user = new User();
            user.setRole(UserRole.GUEST);
        }
        if (AllowedPage.containsValue(requestedPage)
                && AllowedPage.valueOf(requestedPage).isAllowed(user.getRole())) {
            MessageAdder.addMessage(request);
            result = ConfigurationManager.getProperty(AllowedPage.valueOf(requestedPage).getPath());
        }
        return result;
    }
}
