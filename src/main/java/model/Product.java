package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
    private String productId;
    private String productName;
    private String category;
    private Double price;
    private String size;
    private String supplierId;
    private String supplierName;
    private int quantity;

    public Product(
            String productId, String productName, String category, double price, String size, String supplierId, String supplierName, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.size = size;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public int getQuantity() {
        return quantity;
    }
}
