package model;

public class OrderDetail {
    private String itemId;
    private String itemName;
    private int quantity;
    private double price;
    private double total;

    public OrderDetail(String itemId, String itemName, int quantity, double price, double total) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    public String getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getTotal() { return total; }
}
