package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.command.util.MessageAdder;

import javax.servlet.http.HttpServletRequest;

/**
 * Extracts all present messages from the session to the requests and defines the page path to forward to
 */
public class ViewPageCommand implements ActionCommand {
    private static final String PARAM_PAGE_NAME = "page";
    /**
     * Adds messages to the request and returns a page to view
     * @param request request from the servlet which may contain messages in the session
     * @return path to the page to view
     */
    @Override
    public String execute(HttpServletRequest request) {
        MessageAdder.addMessage(request);
        return request.getParameter(PARAM_PAGE_NAME);
    }
}
