// PlaceOrderController.java
package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Optional;

public class PlaceOrderController {

    @FXML private ComboBox<String> cmbCustomerId;
    @FXML private TextField txtCustomerName;
    @FXML private TextField txtCity;

    @FXML private ComboBox<String> cmbItemId;
    @FXML private TextField txtItemName;
    @FXML private TextField txtPrice;
    @FXML private TextField txtAvailableStock;
    @FXML private Spinner<Integer> spnQuantity;

    @FXML private TableView<OrderDetail> tblCart;
    @FXML private TableColumn<OrderDetail, String> colItemId;
    @FXML private TableColumn<OrderDetail, String> colItemName;
    @FXML private TableColumn<OrderDetail, Integer> colQty;
    @FXML private TableColumn<OrderDetail, Double> colPrice;
    @FXML private TableColumn<OrderDetail, Double> colTotal;

    @FXML private Label lblSubtotal;
    @FXML private Label lblTax;
    @FXML private Label lblTotal;

    private ObservableList<OrderDetail> cartList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        tblCart.setItems(cartList);

        SpinnerValueFactory<Integer> qtyFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spnQuantity.setValueFactory(qtyFactory);

        loadCustomerIds();
        loadItemIds();

        cmbCustomerId.setOnAction(e -> fetchCustomerDetails());
        cmbItemId.setOnAction(e -> fetchItemDetails());
    }

    private void loadCustomerIds() {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            ResultSet rs = con.prepareStatement("SELECT customerId FROM customers").executeQuery();
            while (rs.next()) {
                cmbCustomerId.getItems().add(rs.getString("customerId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadItemIds() {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            ResultSet rs = con.prepareStatement("SELECT productId FROM products").executeQuery();
            while (rs.next()) {
                cmbItemId.getItems().add(rs.getString("productId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchCustomerDetails() {
        String id = cmbCustomerId.getValue();
        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement("SELECT name FROM customers WHERE customerId = ?");
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                txtCustomerName.setText(rs.getString("name"));
                //txtCity.setText(rs.getString("city"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchItemDetails() {
        String id = cmbItemId.getValue();
        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement("SELECT productName, price, quantity FROM products WHERE productId = ?");
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                txtItemName.setText(rs.getString("productName"));
                txtPrice.setText(String.valueOf(rs.getDouble("price")));
                txtAvailableStock.setText(String.valueOf(rs.getInt("quantity")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addToCart(ActionEvent actionEvent) {
        String itemId = cmbItemId.getValue();
        String itemName = txtItemName.getText();
        double price = Double.parseDouble(txtPrice.getText());
        int qty = spnQuantity.getValue();
        double total = price * qty;

        for (OrderDetail od : cartList) {
            if (od.getItemId().equals(itemId)) {
                cartList.remove(od);
                qty += od.getQuantity();
                total = price * qty;
                break;
            }
        }

        cartList.add(new OrderDetail(itemId, itemName, qty, price, total));
        tblCart.refresh();
        updateSummary();
    }

    private void updateSummary() {
        double subtotal = 0.0;
        for (OrderDetail item : cartList) {
            subtotal += item.getTotal();
        }

        lblSubtotal.setText(String.format("%.2f", subtotal));
        lblTax.setText("0.00");
        lblTotal.setText(String.format("%.2f", subtotal));
    }


    public void clearOrder(ActionEvent actionEvent) {
        cartList.clear();
        tblCart.refresh();
        updateSummary();
    }
    private String generateOrderId() {
        String newId = "O001"; // default starting ID
        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement("SELECT orderId FROM order_detail ORDER BY orderId DESC LIMIT 1");
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String lastId = rs.getString("orderId"); // e.g., "O009"
                int num = Integer.parseInt(lastId.substring(1)); // 9
                num++;
                newId = String.format("O%03d", num); // O010
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newId;
    }

    public void placeOrder(ActionEvent actionEvent) {
        if (cmbCustomerId.getValue() == null || cartList.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a customer and add items to the cart.").show();
            return;
        }

        String orderId = generateOrderId();
        String customerId = cmbCustomerId.getValue();
        LocalDate orderDate = LocalDate.now();
        double totalAmount = cartList.stream().mapToDouble(OrderDetail::getTotal).sum();

        try {
            Connection con = DBConnection.getInstance().getConnection();

            // 1. Save to orders table
            PreparedStatement orderStmt = con.prepareStatement(
                    "INSERT INTO orders (orderId, customerId, orderDate, totalAmount) VALUES (?, ?, ?, ?)"
            );
            orderStmt.setString(1, orderId);
            orderStmt.setString(2, customerId);
            orderStmt.setDate(3, java.sql.Date.valueOf(orderDate));
            orderStmt.setDouble(4, totalAmount);
            orderStmt.executeUpdate();

            // 2. Save each item to order_details table
            for (OrderDetail item : cartList) {
                PreparedStatement detailStmt = con.prepareStatement(
                        "INSERT INTO order_detail (orderId, itemId, quantity, price, total) VALUES (?, ?, ?, ?, ?)"
                );
                detailStmt.setString(1, orderId);
                detailStmt.setString(2, item.getItemId());
                detailStmt.setInt(3, item.getQuantity());
                detailStmt.setDouble(4, item.getPrice());
                detailStmt.setDouble(5, item.getTotal());
                detailStmt.executeUpdate();
            }

            new Alert(Alert.AlertType.INFORMATION, "Order placed successfully! Order ID: " + orderId).show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to place order!").show();
            return;
        }

        // Clear all fields
        cmbCustomerId.getSelectionModel().clearSelection();
        txtCustomerName.clear();
        cmbItemId.getSelectionModel().clearSelection();
        txtItemName.clear();
        txtPrice.clear();
        txtAvailableStock.clear();
        spnQuantity.getValueFactory().setValue(1);

        cartList.clear();
        tblCart.refresh();
        updateSummary();
    }


}
