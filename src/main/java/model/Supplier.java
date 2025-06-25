package model;

public class Supplier {
    private String supplierId;
    private String supplierName;
    private String contact;
    private String email;

    // Default constructor
    public Supplier() {}

    // Parameterized constructor
    public Supplier(String supplierId, String supplierName, String contact, String email) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contact = contact;
        this.email = email;
    }

    // Getters
    public String getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // toString()
    @Override
    public String toString() {
        return "Supplier{" +
                "supplierId='" + supplierId + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
