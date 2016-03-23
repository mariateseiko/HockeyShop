package by.bsuir.hockeyshop.filter;

import by.bsuir.hockeyshop.managers.MessageManager;

import javax.servlet.*;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class LocaleFilter implements Filter {
    static final String ATTR_MESSAGE_MANAGER = "messageManager";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpSession session = request.getSession();
        if (session.getAttribute(ATTR_MESSAGE_MANAGER) == null) {
            MessageManager messageManager;
            switch(request.getLocale().getLanguage()) {
                case "en":
                    messageManager = MessageManager.EN;
                    break;
                case "ru":
                    messageManager = MessageManager.RU;
                    break;
                default:
                    messageManager = MessageManager.EN;
            }
            session.setAttribute(ATTR_MESSAGE_MANAGER, messageManager);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
