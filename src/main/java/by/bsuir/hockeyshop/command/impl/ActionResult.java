package by.bsuir.hockeyshop.command.impl;

/**
 * Represents possible outcomes of several post commands. Used as a part of the Post-Redirect-Get pattern to pass results
 * of post commands to another commands
 */
public enum ActionResult {
    ITEM_REMOVED,
    ITEM_ADDED_TO_ORDER,
    ORDER_DELETED,
    LOGIN,
    REGISTER,
    REGISTER_VALIDATED,
    ITEM_ADDED_TO_CATALOG,
    ORDER_PAID,
    STATUS_UPDATED,
    PRICE_UPDATED,
    ORDER_SUBMITTED,
    VALIDATION_FAILED
}
