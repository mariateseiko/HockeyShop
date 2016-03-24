package by.bsuir.hockeyshop.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidatorTest {
    @Test
    public void testLoginValidity() {
        assertTrue(Validator.validateLogin("someCliEnT"));
        assertTrue(Validator.validateLogin("test"));
        assertTrue(Validator.validateLogin("test_"));
        assertTrue(Validator.validateLogin("test1test2test3"));
        assertFalse("shouldn't start with a number", Validator.validateLogin("4testtest"));
        assertFalse(Validator.validateLogin("test%"));
        assertFalse("should be at least 4 characters long", Validator.validateLogin("tes"));
        assertFalse("shouldn't be more than 15 characters long", Validator.validateLogin("test1test2test34"));
    }

    @Test
    public void testPasswordValidity() {
        assertTrue(Validator.validatePassword("123DeSew7"));
        assertTrue(Validator.validatePassword("test23"));
        assertTrue(Validator.validatePassword("test1test2test3test7"));
        assertTrue(Validator.validatePassword("test____"));
        assertFalse("should be at least 5 characters long", Validator.validatePassword(("test")));
        assertFalse("shouldn't be more than 20 characters long", Validator.validatePassword(("test1test2test34testtesttest")));
        assertFalse(Validator.validatePassword("_tes$t____"));
    }
}
