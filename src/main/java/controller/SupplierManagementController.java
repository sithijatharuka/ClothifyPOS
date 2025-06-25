package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SupplierManagementController {

    @FXML private TextField txtSupplierId;
    @FXML private TextField txtSupplierName;
    @FXML private TextField txtContact;
    @FXML private TextField txtEmail;

    @FXML private TableView<Supplier> tblSuppliers;
    @FXML private TableColumn<Supplier, String> colSupplierId;
    @FXML private TableColumn<Supplier, String> colSupplierName;
    @FXML private TableColumn<Supplier, String> colContact;
    @FXML private TableColumn<Supplier, String> colEmail;

    @FXML
    public void initialize() {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tblSuppliers.setOnMouseClicked(event -> {
            Supplier selected = tblSuppliers.getSelectionModel().getSelectedItem();
            if (selected != null) {
                txtSupplierId.setText(selected.getSupplierId());
                txtSupplierName.setText(selected.getSupplierName());
                txtContact.setText(selected.getContact());
                txtEmail.setText(selected.getEmail());
            }
        });

        btnReloadClick(null);
    }

    public void btnAddClick(ActionEvent actionEvent) {
        Supplier supplier = new Supplier(
                txtSupplierId.getText(),
                txtSupplierName.getText(),
                txtContact.getText(),
                txtEmail.getText()
        );

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "INSERT INTO suppliers VALUES (?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, supplier.getSupplierId());
            pst.setString(2, supplier.getSupplierName());
            pst.setString(3, supplier.getContact());
            pst.setString(4, supplier.getEmail());

            pst.executeUpdate();
            System.out.println("Supplier added ✅");

            btnReloadClick(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnUpdateClick(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "UPDATE suppliers SET supplierName=?, contact=?, email=? WHERE supplierId=?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, txtSupplierName.getText());
            pst.setString(2, txtContact.getText());
            pst.setString(3, txtEmail.getText());
            pst.setString(4, txtSupplierId.getText());

            pst.executeUpdate();
            System.out.println("Supplier updated ✅");

            btnReloadClick(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnDeleteClick(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "DELETE FROM suppliers WHERE supplierId=?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, txtSupplierId.getText());

            pst.executeUpdate();
            System.out.println("Supplier deleted ✅");

            btnReloadClick(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnSearchClick(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM suppliers WHERE supplierId=?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, txtSupplierId.getText());
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                txtSupplierName.setText(rs.getString("supplierName"));
                txtContact.setText(rs.getString("contact"));
                txtEmail.setText(rs.getString("email"));
                System.out.println("Supplier found ✅");
            } else {
                System.out.println("Supplier not found ❌");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnReloadClick(ActionEvent actionEvent) {
        ObservableList<Supplier> list = FXCollections.observableArrayList();
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            ResultSet rs = connection.prepareStatement("SELECT * FROM suppliers").executeQuery();

            while (rs.next()) {
                list.add(new Supplier(
                        rs.getString("supplierId"),
                        rs.getString("supplierName"),
                        rs.getString("contact"),
                        rs.getString("email")
                ));
            }
            tblSuppliers.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
