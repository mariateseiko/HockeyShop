package by.bsuir.hockeyshop.command.util;

import by.bsuir.hockeyshop.entity.UserRole;

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

    public static boolean containsValue(String page) {
        for (AllowedPage p : AllowedPage.values()) {
            if (p.name().equals(page)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAllowed(UserRole role) {
        return allowedRole == role;
    }

    public String getPath() {
        return path;
    }
}
