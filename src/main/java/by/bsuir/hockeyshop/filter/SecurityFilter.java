package by.bsuir.hockeyshop.filter;

import by.bsuir.hockeyshop.command.CommandName;
import by.bsuir.hockeyshop.entity.User;
import by.bsuir.hockeyshop.entity.UserRole;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filters all requests to the servlet by checking whether the user has corresponding rights to execute
 * a specified command and whether request method is appropriate for the command.
 * If rights are insufficient, request is redirected to the index page.
 */
@WebFilter(urlPatterns = {"/controller"})
public class SecurityFilter implements Filter {
    private static final String COMMAND = "command";
    private static final String ATTR_USER = "user";
    private static final String INDEX = "/index.jsp";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse)servletResponse;
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
                if (!commandName.isRoleAllowed(role)
                        || !commandName.getRequestType().toString().equalsIgnoreCase(request.getMethod())) {
                    response.sendRedirect(request.getContextPath() + INDEX);
                    return;
                }
            } catch (IllegalArgumentException e) {
                response.sendRedirect(request.getContextPath() + INDEX);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
