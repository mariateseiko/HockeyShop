package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.managers.MessageManager;

import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code ChangeLocaleCommand} is an implementation of {@see ActionCommand}
 * for changing locale for the session
 */
public class ChangeLocaleCommand implements ActionCommand {
    static final String PARAM_LOCALE = "locale";
    static final String ATTR_MESSAGE_MANAGER = "messageManager";

    /**
     * Handles request to the servlet by changing the locale for the session
     * @param request request from the servlet, containing the desired locale
     * @return null
     * @throws CommandException
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String locale = request.getParameter(PARAM_LOCALE);
        String localeToApply;
        MessageManager manager;
        switch (locale) {
            case "en":
                manager = MessageManager.EN;
                localeToApply = "en_US";
                break;
            case "ru":
                manager = MessageManager.RU;
                localeToApply = "ru_RU";
                break;
            default:
                manager = MessageManager.EN;
                localeToApply = "en_US";
        }
        request.getSession().setAttribute(PARAM_LOCALE, localeToApply);
        request.getSession().setAttribute(ATTR_MESSAGE_MANAGER, manager);
        return null;
    }
}
