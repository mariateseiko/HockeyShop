package by.bsuir.hockeyshop.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OrderDateTimeTag extends TagSupport {
    private Calendar calendar;

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public int doStartTag() throws JspException {
        if (calendar == null) {
            return SKIP_BODY;
        }
        try {
            Locale locale = pageContext.getRequest().getLocale();
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
            pageContext.getOut().write(formatter.format(calendar.getTime()));
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}
