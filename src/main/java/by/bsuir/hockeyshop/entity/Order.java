package by.bsuir.hockeyshop.entity;

import java.util.*;

/**
 * Class {@code Order} represents a user's order for a list of items. Contains creation date, in case it was already
 * submitted and payment date if it has been paid. Order's owner is also specified. May contain count of items, sum
 * for payment and a marker, specifying wether its payment is late.
 */
public class Order extends Entity {
    private GregorianCalendar creationDateTime;
    private GregorianCalendar paymentDateTime;
    private Map<Item, Integer> items = new HashMap<>();
    private User user;
    private int countOfItems;
    private int paymentSum;
    private boolean isLate;

    public GregorianCalendar getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(GregorianCalendar creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public GregorianCalendar getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(GregorianCalendar paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }

    public Map<Item, Integer> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public void setItems(Map<Item, Integer> items) {
        this.items = items;
    }

    public void addItems(Map<Item, Integer> items) {
        this.items.putAll(items);
    }

    public int getCountOfItems() {
        return countOfItems;
    }

    public void setCountOfItems(int countOfItems) {
        this.countOfItems = countOfItems;
    }

    public int getPaymentSum() {
        return paymentSum;
    }

    public void setPaymentSum(int paymentSum) {
        this.paymentSum = paymentSum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLate() {
        return isLate;
    }

    public void setLate(boolean isLate) {
        this.isLate = isLate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (creationDateTime != null ? !creationDateTime.equals(order.creationDateTime) : order.creationDateTime != null)
            return false;
        if (paymentDateTime != null ? !paymentDateTime.equals(order.paymentDateTime) : order.paymentDateTime != null)
            return false;
        if (items != null ? !items.equals(order.items) : order.items != null) return false;
        return !(user != null ? !user.equals(order.user) : order.user != null);

    }

    @Override
    public int hashCode() {
        int result = creationDateTime != null ? creationDateTime.hashCode() : 0;
        result = 31 * result + (paymentDateTime != null ? paymentDateTime.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
