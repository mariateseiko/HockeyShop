package by.bsuir.hockeyshop.command;

import by.bsuir.hockeyshop.command.impl.*;
import by.bsuir.hockeyshop.managers.MessageManager;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class CommandStorage {
    private HashMap <CommandName, ActionCommand> commands = new HashMap<>();
    private static CommandStorage instance;
    private static AtomicBoolean isNull = new AtomicBoolean(true);
    private static ReentrantLock lock = new ReentrantLock();

    private CommandStorage(){
        commands.put(CommandName.LOGOUT, new LogoutCommand());
        commands.put(CommandName.CATALOG, new ViewCatalogCommand());
        commands.put(CommandName.VIEW_USER_ORDERS, new ViewUserOrdersCommand());
        commands.put(CommandName.VIEW_ORDER_ITEMS, new ViewOrderItemsCommand());
        commands.put(CommandName.VIEW_ALL_ORDERS, new ViewSubmittedOrdersCommand());
        commands.put(CommandName.CHANGE_LOCALE, new ChangeLocaleCommand());
        commands.put(CommandName.VIEW_ITEM, new ViewItemCommand());
        commands.put(CommandName.VIEW_USER, new ViewUserCommand());
        commands.put(CommandName.VIEW_USERS_LIST, new ViewUsersListCommand());
        commands.put(CommandName.VIEW_PAGE, new ViewPageCommand());

        commands.put(CommandName.LOGIN, new LoginCommand());
        commands.put(CommandName.REGISTER, new RegisterCommand());
        commands.put(CommandName.NEW_ITEM, new NewItemCommand());
        commands.put(CommandName.UPDATE_ITEM_PRICE, new UpdateItemPriceCommand());
        commands.put(CommandName.UPDATE_ITEM_STATUS, new UpdateItemStatusCommand());
        commands.put(CommandName.PAY_ORDER, new PayOrderCommand());
        commands.put(CommandName.SUBMIT_ORDER, new SubmitOrderCommand());
        commands.put(CommandName.ADD_TO_ORDER, new AddItemToOrderCommand());
        commands.put(CommandName.REMOVE_FROM_ORDER, new RemoveItemFromOrderCommand());
        commands.put(CommandName.DELETE_ORDER, new DeleteOrderCommand());
        commands.put(CommandName.BAN_USER, new BanUserCommand());
    }

    public static CommandStorage getInstance() {
        if (isNull.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new CommandStorage();
                    isNull.set(false);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public ActionCommand getCommand(HttpServletRequest request) throws CommandException {
        ActionCommand current = new EmptyCommand();
        String action = request.getParameter("command");
        if (action == null || action.isEmpty()) {
            return current;
        }
        try {
            CommandName commandName = CommandName.valueOf(action.toUpperCase());
            current = commands.get(commandName);
        } catch (IllegalArgumentException e) {
            throw new CommandException(e);
        }
        return current;
    }

    public ActionCommand getCommand(String command){
        ActionCommand current = new EmptyCommand();
        if (command == null || command.isEmpty()) {
            return current;
        }
        CommandName commandName = CommandName.valueOf(command.toUpperCase());
        current = commands.get(commandName);
        return current;
    }
}
