package by.bsuir.hockeyshop.command;

import by.bsuir.hockeyshop.entity.UserRole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains names of all ActionCommands, supported by the Controller servlet with corresponding allowed user roles.
 */
public enum CommandName {
    LOGIN(UserRole.GUEST),
    REGISTER(UserRole.GUEST),
    LOGOUT(UserRole.CLIENT, UserRole.ADMIN),
    VIEW_USER_ORDERS(UserRole.CLIENT),
    PAY_ORDER(UserRole.CLIENT),
    SUBMIT_ORDER(UserRole.CLIENT),
    ADD_TO_ORDER(UserRole.CLIENT),
    REMOVE_FROM_ORDER(UserRole.CLIENT),
    DELETE_ORDER(UserRole.CLIENT),
    NEW_ITEM(UserRole.ADMIN),
    UPDATE_ITEM_PRICE(UserRole.ADMIN),
    UPDATE_ITEM_STATUS(UserRole.ADMIN),
    VIEW_ALL_ORDERS(UserRole.ADMIN),
    BAN_USER(UserRole.ADMIN),
    VIEW_USER(UserRole.ADMIN),
    VIEW_USERS_LIST(UserRole.ADMIN),
    VIEW_ORDER_ITEMS(UserRole.CLIENT, UserRole.ADMIN),
    CATALOG(UserRole.GUEST, UserRole.CLIENT, UserRole.ADMIN),
    CHANGE_LOCALE(UserRole.GUEST, UserRole.CLIENT, UserRole.ADMIN),
    VIEW_ITEM(UserRole.GUEST, UserRole.CLIENT, UserRole.ADMIN),
    VIEW_PAGE(UserRole.GUEST, UserRole.CLIENT, UserRole.ADMIN);


    private List<UserRole> allowedUsers = new ArrayList<>();
    CommandName(UserRole... allowedUsers) {
        this.allowedUsers.addAll(Arrays.asList(allowedUsers));
    }

    /**
     * Defines whether a user with it's specified role is allowed to perform the command
     * @param role user's role
     * @return true if user is allowed to perform the command
     */
    public boolean isRoleAllowed(UserRole role) {
        return allowedUsers.contains(role);
    }
}
