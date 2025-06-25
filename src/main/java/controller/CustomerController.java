package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerController {

    @FXML private TextField txtCustomerId;
    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;

    @FXML private TableView<Customer> tblCustomers;
    @FXML private TableColumn<Customer, String> colCustomerId;
    @FXML private TableColumn<Customer, String> colName;
    @FXML private TableColumn<Customer, String> colEmail;
    @FXML private TableColumn<Customer, String> colPhone;

    @FXML
    public void initialize() {
        colCustomerId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("phone"));

        String autoId = generateCustomerId();
        txtCustomerId.setText(autoId);

        tblCustomers.setOnMouseClicked(e -> {
            Customer c = tblCustomers.getSelectionModel().getSelectedItem();
            if (c != null) {
                txtCustomerId.setText(c.getCustomerId());
                txtName.setText(c.getName());
                txtEmail.setText(c.getEmail());
                txtPhone.setText(c.getPhone());
            }
        });

        btnReloadClick();
    }

    private String generateCustomerId() {
        String newId = "C001"; // default starting ID
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pst = connection.prepareStatement(
                    "SELECT customerId FROM customers ORDER BY customerId DESC LIMIT 1"
            );
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("customerId"); // e.g., "C009"
                int num = Integer.parseInt(lastId.substring(1)); // extracts 009 -> 9
                num++;
                newId = String.format("C%03d", num); // C010
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newId;
    }


    public void btnAddClick() {
        Customer customer = new Customer(
                txtCustomerId.getText(),
                txtName.getText(),
                txtEmail.getText(),
                txtPhone.getText()
        );

        try {
            Connection con = DBConnection.getInstance().getConnection();
            String sql = "INSERT INTO customers VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, customer.getCustomerId());
            pst.setString(2, customer.getName());
            pst.setString(3, customer.getEmail());
            pst.setString(4, customer.getPhone());
            pst.executeUpdate();
            btnReloadClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnSearchClick() {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM customers WHERE customerId = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, txtCustomerId.getText());

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                txtName.setText(rs.getString("name"));
                txtEmail.setText(rs.getString("email"));
                txtPhone.setText(rs.getString("phone"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnUpdateClick() {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            String sql = "UPDATE customers SET name = ?, email = ?, phone = ? WHERE customerId = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, txtName.getText());
            pst.setString(2, txtEmail.getText());
            pst.setString(3, txtPhone.getText());
            pst.setString(4, txtCustomerId.getText());
            pst.executeUpdate();
            btnReloadClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnDeleteClick() {
        try {
            Connection con = DBConnection.getInstance().getConnection();
            String sql = "DELETE FROM customers WHERE customerId = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, txtCustomerId.getText());
            pst.executeUpdate();
            btnReloadClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnReloadClick() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        try {
            Connection con = DBConnection.getInstance().getConnection();
            PreparedStatement pst = con.prepareStatement("SELECT * FROM customers");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                customerList.add(new Customer(
                        rs.getString("customerId"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }

            tblCustomers.setItems(customerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
