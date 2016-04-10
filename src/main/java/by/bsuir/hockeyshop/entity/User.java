package by.bsuir.hockeyshop.entity;

import java.util.Objects;

/**
 * Represents a user of the system. Each user has its unique login, along with a password, email and {@code UserRole}
 * role. May contain a phone number. Also has a boolean field, specifying whether user is banned and therefore can't
 * be authorized. May also contain count of orders of different types.
 */
public class User extends Entity {
    private String login;
    private String password;
    private String email;
    private UserRole role;
    private Boolean banned;
    private int countOfSubmittedOrders;
    private int countOfPaidOrders;
    private int countOfUnpaidOrders;
    private int countOfLateOrders;

    public User() {}

    public User(long id) {
        setId(id);
    }

    public User(long id, String login) {
        this.login = login;
        setId(id);
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String login,  String password, String email) {
        this(login, password);
        this.email = email;
    }

    public User(String login, String password, String email, UserRole role) {
        this(login, password, email);
        this.role = role;
    }

    public int getCountOfSubmittedOrders() {
        return countOfSubmittedOrders;
    }

    public void setCountOfSubmittedOrders(int countOfSubmittedOrders) {
        this.countOfSubmittedOrders = countOfSubmittedOrders;
    }

    public int getCountOfPaidOrders() {
        return countOfPaidOrders;
    }

    public void setCountOfPaidOrders(int countOfPaidOrders) {
        this.countOfPaidOrders = countOfPaidOrders;
    }

    public int getCountOfUnpaidOrders() {
        return countOfUnpaidOrders;
    }

    public void setCountOfUnpaidOrders(int countOfUnpaidOrders) {
        this.countOfUnpaidOrders = countOfUnpaidOrders;
    }

    public int getCountOfLateOrders() {
        return countOfLateOrders;
    }

    public void setCountOfLateOrders(int countOfLateOrders) {
        this.countOfLateOrders = countOfLateOrders;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Boolean isBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!login.equals(user.login)) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (role != user.role) return false;
        return !(banned != null ? !banned.equals(user.banned) : user.banned != null);

    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (banned != null ? banned.hashCode() : 0);
        return result;
    }
}
