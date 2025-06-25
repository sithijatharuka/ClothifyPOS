package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductManagementController {

    @FXML private TextField txtpId;
    @FXML private TextField txtpName;
    @FXML private ComboBox<String> cmbcategory;
    @FXML private TextField txtprice;
    @FXML private TextField txtsize;
    @FXML private ComboBox<String> cmbSupplierId;
    @FXML private TextField txtsuplierName;
    @FXML private TextField txtqty;

    @FXML private TableView<Product> tblProducts;
    @FXML private TableColumn<Product, String> colProductId;
    @FXML private TableColumn<Product, String> colProductName;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colQuantity;
    @FXML private TableColumn<Product, String> colSize;
    @FXML private TableColumn<Product, String> colSupplierId;
    @FXML private TableColumn<Product, String> colSupplierName;

    @FXML
    public void initialize() {
        cmbcategory.setItems(FXCollections.observableArrayList("Tops", "Bottoms", "Dresses","Outerwear","Undergarments","Traditional"));

        String autoId = generateProductId();
        txtpId.setText(autoId);

        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));

        loadSupplierIds();

        tblProducts.setOnMouseClicked(event -> {
            Product selected = tblProducts.getSelectionModel().getSelectedItem();
            if (selected != null) {
                txtpId.setText(selected.getProductId());
                txtpName.setText(selected.getProductName());
                cmbcategory.setValue(selected.getCategory());
                txtprice.setText(String.valueOf(selected.getPrice()));
                txtsize.setText(selected.getSize());
                cmbSupplierId.setValue(selected.getSupplierId());
                txtsuplierName.setText(selected.getSupplierName());
                txtqty.setText(String.valueOf(selected.getQuantity()));
            }
        });

        cmbSupplierId.setOnAction(e -> loadSupplierNameById(cmbSupplierId.getValue()));
    }

    private void loadSupplierIds() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "SELECT supplierId FROM suppliers";
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            ObservableList<String> supplierIds = FXCollections.observableArrayList();
            while (rs.next()) {
                supplierIds.add(rs.getString("supplierId"));
            }
            cmbSupplierId.setItems(supplierIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSupplierNameById(String supplierId) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "SELECT supplierName FROM suppliers WHERE supplierId = ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, supplierId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                txtsuplierName.setText(rs.getString("supplierName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnAddClick(ActionEvent actionEvent) {
        Product product = new Product(
                txtpId.getText(),
                txtpName.getText(),
                cmbcategory.getValue(),
                Double.parseDouble(txtprice.getText()),
                txtsize.getText(),
                cmbSupplierId.getValue(),
                txtsuplierName.getText(),
                Integer.parseInt(txtqty.getText())
        );

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "INSERT INTO products VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setString(1, product.getProductId());
            pst.setString(2, product.getProductName());
            pst.setString(3, product.getCategory());
            pst.setDouble(4, product.getPrice());
            pst.setString(5, product.getSize());
            pst.setString(6, product.getSupplierId());
            pst.setString(7, product.getSupplierName());
            pst.setInt(8, product.getQuantity());

            pst.executeUpdate();
            System.out.println("Inserted to DB ✅");
            btnReloadClick(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnUpdateClick(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "UPDATE products SET productName=?, category=?, price=?, size=?, supplierId=?, supplierName=?, quantity=? WHERE productId=?";
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setString(1, txtpName.getText());
            pst.setString(2, cmbcategory.getValue());
            pst.setDouble(3, Double.parseDouble(txtprice.getText()));
            pst.setString(4, txtsize.getText());
            pst.setString(5, cmbSupplierId.getValue());
            pst.setString(6, txtsuplierName.getText());
            pst.setInt(7, Integer.parseInt(txtqty.getText()));
            pst.setString(8, txtpId.getText());

            int result = pst.executeUpdate();
            if (result > 0) {
                System.out.println("Updated ✅");
                btnReloadClick(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnDeleteClick(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "DELETE FROM products WHERE productId = ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, txtpId.getText());

            int result = pst.executeUpdate();
            if (result > 0) {
                System.out.println("Deleted ✅");
                btnReloadClick(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnSeacrhClick(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM products WHERE productId = ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, txtpId.getText());

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                txtpName.setText(rs.getString("productName"));
                cmbcategory.setValue(rs.getString("category"));
                txtprice.setText(String.valueOf(rs.getDouble("price")));
                txtsize.setText(rs.getString("size"));
                cmbSupplierId.setValue(rs.getString("supplierId"));
                txtsuplierName.setText(rs.getString("supplierName"));
                txtqty.setText(String.valueOf(rs.getInt("quantity")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateProductId() {
        String newId = "P001"; // default
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pst = connection.prepareStatement("SELECT productId FROM products ORDER BY productId DESC LIMIT 1");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("productId"); // e.g., "P009"
                int num = Integer.parseInt(lastId.substring(1)); // 9
                num++;
                newId = String.format("P%03d", num); // P010
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newId;
    }


    public void btnReloadClick(ActionEvent actionEvent) {
        ObservableList<Product> list = FXCollections.observableArrayList();
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            ResultSet rs = connection.prepareStatement("SELECT * FROM products").executeQuery();
            while (rs.next()) {
                list.add(new Product(
                        rs.getString("productId"),
                        rs.getString("productName"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getString("size"),
                        rs.getString("supplierId"),
                        rs.getString("supplierName"),
                        rs.getInt("quantity")
                ));
            }
            tblProducts.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
