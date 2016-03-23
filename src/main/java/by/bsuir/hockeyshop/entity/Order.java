package by.bsuir.hockeyshop.entity;

import java.util.*;

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
}
