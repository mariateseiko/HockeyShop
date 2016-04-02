package by.bsuir.hockeyshop.entity;

/**
 * Class {@code Item} represents an item in the shop's catalog. Each item has a name, color, size, price, status and type.
 * Path to image, additional details and description can also be added.
 */
public class Item extends Entity{
    private String name;
    private String color;
    private String size;
    private int price;
    private ItemStatus status;
    private ItemType type;
    private String imagePath;
    private String additionalInfo;
    private String description;

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public ItemType getType() { return type; }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (price != item.price) return false;
        if (additionalInfo != null ? !additionalInfo.equals(item.additionalInfo) : item.additionalInfo != null)
            return false;
        if (!color.equals(item.color)) return false;
        if (description != null ? !description.equals(item.description) : item.description != null) return false;
        if (imagePath != null ? !imagePath.equals(item.imagePath) : item.imagePath != null) return false;
        if (!name.equals(item.name)) return false;
        if (!size.equals(item.size)) return false;
        if (status != item.status) return false;
        if (type != item.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + color.hashCode();
        result = 31 * result + size.hashCode();
        result = 31 * result + price;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (imagePath != null ? imagePath.hashCode() : 0);
        result = 31 * result + (additionalInfo != null ? additionalInfo.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
