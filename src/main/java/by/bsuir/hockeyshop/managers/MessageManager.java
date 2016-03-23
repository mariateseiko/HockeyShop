package by.bsuir.hockeyshop.managers;

import java.util.Locale;
import java.util.ResourceBundle;

public enum MessageManager {
    EN("en-US"), RU("ru-RU");
    private ResourceBundle resourceBundle;

    MessageManager(String locale) {
        resourceBundle = ResourceBundle.getBundle("message", Locale.forLanguageTag(locale));
    }

    public String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
