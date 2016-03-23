package by.bsuir.hockeyshop.tag;

import by.bsuir.hockeyshop.entity.User;
import by.bsuir.hockeyshop.entity.UserRole;
import by.bsuir.hockeyshop.managers.ConfigurationManager;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class MenuTag extends TagSupport {
    static final String ATTR_USER = "user";
    @Override
    public int doStartTag()  throws JspException{
        try {
            User currentUser = (User) pageContext.getSession().getAttribute(ATTR_USER);
            if (currentUser != null) {
                if (UserRole.ADMIN == currentUser.getRole()) {
                    pageContext.include(ConfigurationManager.getProperty("path.page.admin.header"));
                } else if (UserRole.CLIENT == currentUser.getRole()) {
                    pageContext.include(ConfigurationManager.getProperty("path.page.client.header"));
                }
            } else {
                pageContext.include(ConfigurationManager.getProperty("path.page.guest.header"));
            }

        } catch (ServletException|IOException e) {
            throw new JspTagException(e);
        }
        return SKIP_BODY;
    }
}
