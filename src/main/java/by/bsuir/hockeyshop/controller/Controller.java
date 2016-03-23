package by.bsuir.hockeyshop.controller;

import by.bsuir.hockeyshop.command.ActionCommand;
import by.bsuir.hockeyshop.command.CommandStorage;
import by.bsuir.hockeyshop.command.CommandException;
import by.bsuir.hockeyshop.managers.ConfigurationManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/controller")
@MultipartConfig
public class Controller extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page;
        CommandStorage storage = CommandStorage.getInstance();
        ActionCommand command = storage.getCommand(req);
        try {
            page = command.execute(req);
        } catch (CommandException e) {
            page = ConfigurationManager.getProperty("path.page.systemerror");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
            dispatcher.forward(req, resp);
        }

        if (page != null) {
            if(req.getMethod().equalsIgnoreCase("get")){
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
                dispatcher.forward(req, resp);
            } else {
                resp.sendRedirect(req.getContextPath()+page);
            }
        } else {
            page = req.getHeader("Referer");
            resp.sendRedirect(page);}
        }
    }

