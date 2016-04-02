package by.bsuir.hockeyshop.entity;

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
    private String phone;
    private String address;
    private Boolean banned;
    private int countOfSubmittedOrders;
    private int countOfPaidOrders;
    private int countOfUnpaidOrders;
    private int countOfLateOrders;

    public User() {}

    public User(long id) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean isBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", phone='" + phone + '\'' +
                ", banned=" + banned +
                '}';
    }
}
