package by.bsuir.hockeyshop.command.util;

import by.bsuir.hockeyshop.entity.Item;
import by.bsuir.hockeyshop.entity.User;

import javax.servlet.http.Part;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An utility class for validation purposes. Offers a variety of static methods to check validity of input values
 * and objects
 */
public class Validator {
    private static final String REGEXP_LOGIN = "^[A-z]\\w{3,14}$";
    private static final String REGEXP_PASSWORD = "^[A-z0-9]\\w{5,19}$";
    private static final String REGEXP_EMAIL  = "^(\\w+)@(\\w+\\.)([a-z]{1,4})$";
    private static final String REGEXP_ITEM_NAME = "^[А-я\\w-\\/ ]{3,40}$";
    private static final String REGEXP_COLOR = "^[А-я\\w-\\/ ]{1,12}$";
    private static final String REGEXP_SIZE = "^[А-я\\w-\\/ ]{1,12}$";
    private static final String REGEXP_CARD = "^[0-9]{16}$";
    private static final String REGEXP_QUANTITY = "^[1-9][0]?$";
    private static final String REGEXP_PRICE = "^[1-9][0-9]{0,4}?$";
    private static final String REGEXP_FILE_NAME = "^.+\\.(jp(e)?g)||(JP(E)?G)||(png)||(PNG)$";
    private static final int MAX_INFO_LENGTH = 255;
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * Validates fields of a given user object
     * @param user object to validate
     * @return {@code true} if login, password and email are not null and valid and phone is null or valid
     */
    public static boolean validateUser(User user) {
        return validateLogin(user.getLogin()) && validatePassword(user.getPassword())
                && validateEmail(user.getEmail());
    }

    /**
     * Validates fields of a given item object, a string, representing price and a request part containing item's image.
     * A valid item is an item where name, color, price and size are not null and valid according to their regular expression.
     * Additional info and description may be absent, but if present, their length shouldn't exceed MAX_INFO_LENGTH. Part
     * may be null, but if presents, its size mustn't be bigger than MAX_FILE_SIZE and be of JPEG or PNG format. If any of these
     * rules isn't followed, the whole item is considered as invalid.
     * @param item object to validate
     * @return {@code true} if item is valid
     */
    public static boolean validateItem(Item item, String price, Part part) {
        return validateItemName(item.getName()) && validateColor(item.getColor())
                && validateSize(item.getSize()) && validatePrice(price)
                && validateAdditionalInfo(item.getAdditionalInfo())
                && validateDescription(item.getDescription())
                && validatePart(part);
    }

    /**
     * Validates a given string by a passed regex
     * @param value string to validate
     * @param regex regular expression of a valid string
     * @return true if valid, false if not
     */
    public static boolean validate(String value, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);
        return m.matches();
    }

    public static boolean validateLogin(String login) {
        return validate(login, REGEXP_LOGIN);
    }

    public static boolean validatePassword(String password) {
        return validate(password, REGEXP_PASSWORD);
    }

    public static boolean validateEmail(String email) {
        return validate(email, REGEXP_EMAIL);
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

    public static boolean validateCard(String card) { return validate(card, REGEXP_CARD); }

    public static boolean validateQuantity(String quantity) {
        return validate(quantity, REGEXP_QUANTITY) && Integer.parseInt(quantity) > 0 && Integer.parseInt(quantity) <= 10;
    }

    public static boolean validatePrice(String quantity) {
        return validate(quantity, REGEXP_PRICE);
    }

    public static boolean validateAdditionalInfo(String additional) {
        return additional == null || additional.length() <= MAX_INFO_LENGTH;
    }

    public static boolean validateDescription(String description) {
        return description == null || description.length() <= MAX_INFO_LENGTH;
    }

    public static boolean validatePart(Part part) {
        return part == null || (part.getSize() > 0 && part.getSize() < MAX_FILE_SIZE
                && validate(part.getSubmittedFileName(), REGEXP_FILE_NAME));
    }
}
