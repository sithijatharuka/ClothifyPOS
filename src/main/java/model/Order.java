package model;

import java.time.LocalDate;

public class Order {
    private String orderId;
    private String customerId;
    private LocalDate orderDate;
    private double totalAmount;

    public Order(String orderId, String customerId, LocalDate orderDate, double totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
