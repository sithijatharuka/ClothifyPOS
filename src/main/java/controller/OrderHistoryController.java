package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class OrderHistoryController {

    @FXML private TableView<Order> tblOrders;
    @FXML private TableColumn<Order, String> colOrderId;
    @FXML private TableColumn<Order, String> colCustomerId;
    @FXML private TableColumn<Order, LocalDate> colOrderDate;
    @FXML private TableColumn<Order, Double> colTotalAmount;

    @FXML
    public void initialize() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        loadOrders();
    }

    private void loadOrders() {
        ObservableList<Order> orders = FXCollections.observableArrayList();

        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement("SELECT orderId, customerId, orderDate, totalAmount FROM orders ORDER BY orderDate DESC");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String orderId = rs.getString("orderId");
                String customerId = rs.getString("customerId");
                LocalDate orderDate = rs.getDate("orderDate").toLocalDate();
                double totalAmount = rs.getDouble("totalAmount");

                orders.add(new Order(orderId, customerId, orderDate, totalAmount));
            }

            tblOrders.setItems(orders);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
