package by.bsuir.hockeyshop.util;

import by.bsuir.hockeyshop.entity.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    static final String REGEXP_LOGIN = "[A-z]\\w{3,14}";
    static final String REGEXP_PASSWORD = "[A-z0-9]\\w{5,19}";
    static final String REGEXP_EMAIL  = "(\\w+)@(\\w+\\.)([a-z]{1,4})";
    static final String REGEXP_PHONE  = "(\\d){4,20}";

    public static boolean validateLogin(String login) {
        return validate(login, REGEXP_LOGIN);
    }

    public static boolean validatePassword(String login) {
        return validate(login, REGEXP_PASSWORD);
    }

    public static boolean validateEmail(String login) {
        return validate(login, REGEXP_EMAIL);
    }

    public static boolean validatePhone(String login) {
        return validate(login, REGEXP_PHONE);
    }

    public static boolean validateUser(User user) {
        boolean isPhoneValid = user.getPhone()==null || validatePhone(user.getPhone());
        return validateLogin(user.getLogin()) && validatePassword(user.getPassword())
                && validateEmail(user.getEmail()) && isPhoneValid;
    }

    public static boolean validate(String value, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);
        return m.matches();
    }
}
