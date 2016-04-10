package by.bsuir.hockeyshop.entity;

/**
 * Represents item's status in the shop.
 */
public enum ItemStatus {
    IN_STOCK("label.instock"),
    OUT_OF_STOCK("label.outofstock"),
    OUT_OF_PRODUCTION("label.outofproduction");

    private String name;

    ItemStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
