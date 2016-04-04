package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.command.MessageAdder;
import by.bsuir.hockeyshop.managers.MessageManager;

import javax.servlet.http.HttpServletRequest;

public class ViewPageCommand implements ActionCommand {
    private static final String PARAM_PAGE_NAME = "page";
    static final String ATTR_MESSAGE_MANAGER = "messageManager";

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        MessageAdder.addMessage(request);
        return request.getParameter(PARAM_PAGE_NAME);
    }
}
