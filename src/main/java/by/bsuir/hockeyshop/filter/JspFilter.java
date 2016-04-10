package by.bsuir.hockeyshop.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filters all requests to the servlet by checking whether the encoding matches the encoding specified in the init params
 * of the filter. If n
 */
@WebFilter(urlPatterns = {"/jsp/common/*", "/jsp/error/*", "/jsp/header/*", "/jsp/admin/*"})
public class JspFilter implements Filter {
    private static final String INDEX = "/index.jsp";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        response.sendRedirect(request.getContextPath() + INDEX);
    }

    @Override
    public void destroy() {
    }
}
