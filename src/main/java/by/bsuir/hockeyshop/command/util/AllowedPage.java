package by.bsuir.hockeyshop.command.util;

import by.bsuir.hockeyshop.entity.UserRole;

/**
 * This enum represents jsp pages that can be viewed directly by a certain roles of the users
 */
public enum AllowedPage {
    NEW_ITEM("path.page.admin.newitem", UserRole.ADMIN),
    LOGIN("path.page.login", UserRole.GUEST),
    REGISTER("path.page.register", UserRole.GUEST);

    private String path;
    private UserRole allowedRole;

    AllowedPage(String path, UserRole allowedRole) {
        this.path = path;
        this.allowedRole = allowedRole;
    }

    /**
     * Defines whether enum contain a value for a corresponding string
     * @param page string with a page name
     * @return true if string can be converted to a enum value
     */
    public static boolean containsValue(String page) {
        for (AllowedPage p : AllowedPage.values()) {
            if (p.name().equals(page)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Defines whether a user with a given role can view the page
     * @param role role of the user trying to view the page
     * @return if user's role is sufficient to view the page
     */
    public boolean isAllowed(UserRole role) {
        return allowedRole == role;
    }

    public String getPath() {
        return path;
    }
}
