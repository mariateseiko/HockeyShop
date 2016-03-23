package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.managers.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code LogoutCommand} is an implementation of {@see ActionCommand}
 * for signing out
 */
public class LogoutCommand implements ActionCommand {
    /**
     * Handles request to the servlet by invalidating the current session
     * @param request request from the servlet
     * @return path to index jsp page
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page = ConfigurationManager.getProperty("path.page.index");
        request.getSession().invalidate();
        return page;
    }
}
