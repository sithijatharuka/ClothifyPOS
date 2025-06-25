package model;

import java.time.LocalDate;

public class OrderReport {
    private String orderId;
    private String customerId;
    private LocalDate orderDate;
    private String itemId;
    private int quantity;
    private double price;
    private double total;

    public OrderReport(String orderId, String customerId, LocalDate orderDate, String itemId, int quantity, double price, double total) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public LocalDate getOrderDate() { return orderDate; }
    public String getItemId() { return itemId; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getTotal() { return total; }
}
