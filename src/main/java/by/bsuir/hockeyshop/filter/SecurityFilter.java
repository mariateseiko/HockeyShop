package by.bsuir.hockeyshop.filter;

import by.bsuir.hockeyshop.command.CommandName;
import by.bsuir.hockeyshop.entity.User;
import by.bsuir.hockeyshop.entity.UserRole;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = {"/controller"})
public class SecurityFilter implements Filter {
    static final String COMMAND = "command";
    static final String ATTR_USER = "user";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        User user = (User)request.getSession().getAttribute(ATTR_USER);
        UserRole role;
        if (user == null) {
            role = UserRole.GUEST;
        } else {
            role = user.getRole();
        }
        String action = request.getParameter(COMMAND);
        if (action != null) {
            try {
                CommandName commandName = CommandName.valueOf(action.toUpperCase());
                if (!commandName.isRoleAllowed(role)) {
                    RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/index.jsp");
                    dispatcher.forward(servletRequest, servletResponse);
                    return;
                }
            } catch (IllegalArgumentException e) {
                RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/index.jsp");
                dispatcher.forward(servletRequest, servletResponse);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
