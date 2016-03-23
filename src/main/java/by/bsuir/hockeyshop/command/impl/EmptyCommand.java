package by.bsuir.hockeyshop.command.impl;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.managers.ConfigurationManager;
import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code EmptyCommand} is an implementation of {@see ActionCommand}
 * representing default action in case command is not specified
 */
public class EmptyCommand implements ActionCommand {
    /**
     * Handles request to the servlet by returning the default page
     * @param request request from the servlet
     * @return path to the default page
     */
    @Override
    public String execute(HttpServletRequest request) {
        return ConfigurationManager.getProperty("path.page.index");
    }
}
