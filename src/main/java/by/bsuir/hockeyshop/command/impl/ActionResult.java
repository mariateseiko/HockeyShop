package by.bsuir.hockeyshop.command.impl;

/**
 * Represents possible outcomes of several post commands. Used as a part of the Post-Redirect-Get pattern to pass results
 * of post commands to another commands. Each result action contains a key to the error message and may contain a key to
 * the success message and corresponding getters
 */
public enum ActionResult {
    ITEM_REMOVED("message.order.remove.item.success", "message.order.remove.item.error"),
    ITEM_ADDED_TO_ORDER("message.order.add.item.success", "message.order.add.item.error"),
    ORDER_DELETED("message.order.delete.success", "message.order.delete.error"),
    LOGIN("message.login.error"),
    REGISTER("message.register.success", "message.register.error"),
    REGISTER_VALIDATED("message.registration.validation.error"),
    ITEM_ADDED_TO_CATALOG("message.item.add.success", "message.item.add.error"),
    ORDER_PAID("message.order.pay.success", "message.order.pay.error"),
    STATUS_UPDATED("message.update.status.success", "message.update.status.error"),
    PRICE_UPDATED("message.price.update.success", "message.price.update.error"),
    LATE_ORDER_DELETED("message.order.delete.success", "message.order.late.delete.error"),
    ORDER_SUBMITTED("message.order.submit.success", "message.order.submit.error"),
    VALIDATION_FAILED("message.item.add.validation.error");

    private String successKey;
    private String errorKey;

    ActionResult(String successKey, String errorKey) {
        this.successKey = successKey;
        this.errorKey = errorKey;
    }

    ActionResult(String errorKey) {
        this.errorKey = errorKey;
    }

    public String getSuccessKey() {
        return successKey;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
