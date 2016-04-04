package by.bsuir.hockeyshop.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class SessionListener implements HttpSessionListener {
    private static Map<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

    public static Map<String, HttpSession> getSessionMap() {
        return Collections.unmodifiableMap(sessionMap);
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = sessionMap.put(event.getSession().getId(), event.getSession());
        if (session != null) {
            session.invalidate();
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        sessionMap.remove(event.getSession().getId());
    }
}
