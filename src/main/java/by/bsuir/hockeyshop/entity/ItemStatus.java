package by.bsuir.hockeyshop.entity;

public enum ItemStatus {
    IN_STOCK("in stock"),
    OUT_OF_STOCK("out of stock"),
    OUT_OF_PRODUCTION("out of production");

    private String name;

    ItemStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
