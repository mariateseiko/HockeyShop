package by.bsuir.hockeyshop.command;
import static org.junit.Assert.*;

import by.bsuir.hockeyshop.entity.UserRole;
import org.junit.Test;
public class CommandNameTest {
    @Test
    public void isRoleAllowed() {
        assertTrue(CommandName.BAN_USER.isRoleAllowed(UserRole.ADMIN));
        assertFalse(CommandName.BAN_USER.isRoleAllowed(UserRole.CLIENT));
        assertFalse(CommandName.BAN_USER.isRoleAllowed(UserRole.GUEST));
        assertTrue(CommandName.CATALOG.isRoleAllowed(UserRole.ADMIN));
        assertTrue(CommandName.CATALOG.isRoleAllowed(UserRole.CLIENT));
        assertTrue(CommandName.CATALOG.isRoleAllowed(UserRole.GUEST));
        assertFalse(CommandName.ADD_TO_ORDER.isRoleAllowed(UserRole.ADMIN));
        assertTrue(CommandName.ADD_TO_ORDER.isRoleAllowed(UserRole.CLIENT));
        assertFalse(CommandName.ADD_TO_ORDER.isRoleAllowed(UserRole.GUEST));
    }
}
