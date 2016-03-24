package by.bsuir.hockeyshop.command;

import by.bsuir.hockeyshop.command.impl.EmptyCommand;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CommandStorageTest {
    private CommandStorage storage;
    @Before
    public void before() {
        storage = CommandStorage.getInstance();
    }

    @Test
    public void getInstanceTest() {
        assertNotNull(storage);
    }

    @Test
    public void emptyCommandParameterTest() throws CommandException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn("");
        assertEquals(EmptyCommand.class, (storage.getCommand(request)).getClass());
    }

    @Test
    public void notPresentCommandParameterTest() throws CommandException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn(null);
        assertEquals(EmptyCommand.class, (storage.getCommand(request)).getClass());
    }

    @Test
    public void presentCommandParameterTest() throws CommandException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        CommandName[] names = CommandName.values();
        for (CommandName name: names) {
            when(request.getParameter("command")).thenReturn(name.toString());
            assertNotNull(storage.getCommand(name.toString()));
            assertNotNull(storage.getCommand(request));
            assertEquals(storage.getCommand(name.toString()), (storage.getCommand(request)));
        }
    }

    @Test(expected = CommandException.class)
    public void nonExistentCommandTest() throws CommandException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn("dummy_command");
        storage.getCommand(request);
    }

}
