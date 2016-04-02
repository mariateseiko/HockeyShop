package by.bsuir.hockeyshop.controller;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandStorage;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.managers.ConfigurationManager;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles all incoming http-requests, using command pattern.
 */
@WebServlet("/controller")
@MultipartConfig
public class Controller extends HttpServlet {
    final static Logger LOG = Logger.getLogger(Controller.class);
    private CommandStorage storage;

    @Override
    public void init() throws ServletException {
        storage = CommandStorage.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    /**
     * Defines desired action from request, by extracting the specified command from the {@code CommandStorage}.
     * Depending on the request's method and command's execution results, forward or redirection take place
     * @param req an {@link HttpServletRequest} from the client
     * @param resp servlet's response
     * @throws ServletException if the request could not be handled
     * @throws IOException if an input or output error is detected when the servlet handles the GET request
     */
    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page;
        try {
            ActionCommand command = storage.getCommand(req);
            page = command.execute(req);
            if (page != null) {
                if (req.getMethod().equalsIgnoreCase("get")) {
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
                    dispatcher.forward(req, resp);
                } else {
                    resp.sendRedirect(req.getContextPath() + page);
                }
            } else {
                page = req.getHeader("Referer");
                resp.sendRedirect(page);
            }
        } catch(CommandException e) {
            LOG.error("Command execution failed", e);
            resp.sendError(500);
        }
    }
}

