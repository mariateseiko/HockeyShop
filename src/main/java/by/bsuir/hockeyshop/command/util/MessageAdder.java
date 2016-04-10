package by.bsuir.hockeyshop.command.util;

import by.bsuir.hockeyshop.managers.MessageManager;

import javax.servlet.http.HttpServletRequest;

/**
 * Class {@code MessageAdder} extracts info from the request and adds corresponding message
 */
public class MessageAdder {
    static final String ATTR_SUCCESS = "successMessage";
    static final String ATTR_ERROR = "errorMessage";
    static final String ATTR_MESSAGE_MANAGER = "messageManager";

    /**
     * Extracts action result from the request. If it's not null, defines a corresponding message by a key from the
     * action and sets it as an request attribute
     * @param request request which may contain action result {@code ActionResult}
     */
    public static void addMessage(HttpServletRequest request) {
        MessageManager messageManager = (MessageManager)(request.getSession().getAttribute(ATTR_MESSAGE_MANAGER));
        ActionResult success = (ActionResult) request.getSession().getAttribute(ATTR_SUCCESS);
        String message;
        if (success!= null) {
            message = messageManager.getProperty(success.getSuccessKey());
            request.getSession().removeAttribute(ATTR_SUCCESS);
            request.setAttribute(ATTR_SUCCESS, message);
        } else {
            ActionResult error = (ActionResult) request.getSession().getAttribute(ATTR_ERROR);
            if (error != null) {
                message = messageManager.getProperty(error.getErrorKey());
                request.setAttribute(ATTR_ERROR, message);
                request.getSession().removeAttribute(ATTR_ERROR);
            }
        }
    }
}
