package by.bsuir.hockeyshop.util;

import by.bsuir.hockeyshop.entity.Item;
import by.bsuir.hockeyshop.entity.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    static final String REGEXP_LOGIN = "[A-z]\\w{3,14}";
    static final String REGEXP_PASSWORD = "[A-z0-9]\\w{5,19}";
    static final String REGEXP_EMAIL  = "(\\w+)@(\\w+\\.)([a-z]{1,4})";
    static final String REGEXP_PHONE  = "(\\d){4,20}";
    static final String REGEXP_ITEM_NAME = "\\w{3, 40}";
    static final String REGEXP_COLOR = "\\w{1, 20}";
    static final String REGEXP_SIZE = "\\w{1, 20}";
    static final String REGEXP_PRICE = "\\d{1, 5}";

    public static boolean validateLogin(String login) {
        return validate(login, REGEXP_LOGIN);
    }

    public static boolean validatePassword(String password) {
        return validate(password, REGEXP_PASSWORD);
    }

    public static boolean validateEmail(String email) {
        return validate(email, REGEXP_EMAIL);
    }

    public static boolean validatePhone(String phone) {
        return validate(phone, REGEXP_PHONE);
    }

    public static boolean validateItemName(String name) {
        return validate(name, REGEXP_ITEM_NAME);
    }

    public static boolean validateColor(String color) {
        return validate(color, REGEXP_COLOR);
    }

    public static boolean validateSize(String size) {
        return validate(size, REGEXP_SIZE);
    }


    public static boolean validateUser(User user) {
        boolean isPhoneValid = user.getPhone()== null || validatePhone(user.getPhone());
        return validateLogin(user.getLogin()) && validatePassword(user.getPassword())
                && validateEmail(user.getEmail()) && isPhoneValid;
    }

    public static boolean validateItem(Item item) {
        return validateItemName(item.getName()) && validateColor(item.getColor()) && validateSize(item.getSize());
    }

    public static boolean validate(String value, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);
        return m.matches();
    }
}
