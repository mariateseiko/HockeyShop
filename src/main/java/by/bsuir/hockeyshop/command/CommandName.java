package by.bsuir.hockeyshop.command;

import by.bsuir.hockeyshop.command.util.RequestType;
import by.bsuir.hockeyshop.entity.UserRole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains names of all ActionCommands, supported by the Controller servlet with corresponding allowed user roles.
 */
public enum CommandName {
    LOGIN(RequestType.POST, UserRole.GUEST),
    REGISTER(RequestType.POST, UserRole.GUEST),
    PAY_ORDER(RequestType.POST, UserRole.CLIENT),
    SUBMIT_ORDER(RequestType.POST, UserRole.CLIENT),
    ADD_TO_ORDER(RequestType.POST, UserRole.CLIENT),
    REMOVE_FROM_ORDER(RequestType.POST, UserRole.CLIENT),
    DELETE_ORDER(RequestType.POST, UserRole.CLIENT, UserRole.ADMIN),
    NEW_ITEM(RequestType.POST, UserRole.ADMIN),
    UPDATE_ITEM_PRICE(RequestType.POST, UserRole.ADMIN),
    UPDATE_ITEM_STATUS(RequestType.POST, UserRole.ADMIN),
    VIEW_ALL_ORDERS(RequestType.GET, UserRole.ADMIN),
    BAN_USER(RequestType.POST, UserRole.ADMIN),
    VIEW_USER(RequestType.GET, UserRole.ADMIN),
    VIEW_USERS_LIST(RequestType.GET, UserRole.ADMIN),
    VIEW_ORDER_ITEMS(RequestType.GET, UserRole.CLIENT, UserRole.ADMIN),
    LOGOUT(RequestType.GET, UserRole.CLIENT, UserRole.ADMIN),
    VIEW_USER_ORDERS(RequestType.GET, UserRole.CLIENT, UserRole.ADMIN),
    CATALOG(RequestType.GET, UserRole.GUEST, UserRole.CLIENT, UserRole.ADMIN),
    CHANGE_LOCALE(RequestType.GET, UserRole.GUEST, UserRole.CLIENT, UserRole.ADMIN),
    VIEW_ITEM(RequestType.GET, UserRole.GUEST, UserRole.CLIENT, UserRole.ADMIN),
    VIEW_PAGE(RequestType.GET, UserRole.GUEST, UserRole.CLIENT, UserRole.ADMIN);

    private List<UserRole> allowedUsers = new ArrayList<>();
    private RequestType type;
    CommandName(RequestType type, UserRole... allowedUsers) {
        this.type = type;
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

    public RequestType getRequestType() {
        return type;
    }
}


