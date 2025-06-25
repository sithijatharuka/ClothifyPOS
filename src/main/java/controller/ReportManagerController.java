package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.OrderReport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class ReportManagerController {

    @FXML
    private TableView<OrderReport> tblReports;
    @FXML
    private TableColumn<OrderReport, String> colOrderId;
    @FXML
    private TableColumn<OrderReport, String> colCustomerId;
    @FXML
    private TableColumn<OrderReport, LocalDate> colOrderDate;
    @FXML
    private TableColumn<OrderReport, String> colItemId;
    @FXML
    private TableColumn<OrderReport, Integer> colQuantity;
    @FXML
    private TableColumn<OrderReport, Double> colPrice;
    @FXML
    private TableColumn<OrderReport, Double> colTotal;

    private ObservableList<OrderReport> reportData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set cell value factories to match the OrderReport model
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Set the items to the table
        tblReports.setItems(reportData);

        // Load data from database
        loadReportData();
    }

    private void loadReportData() {
        String sql = "SELECT o.orderId, o.customerId, o.orderDate, d.itemId, d.quantity, d.price, d.total " +
                "FROM orders o " +
                "JOIN order_detail d ON o.orderId = d.orderId";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                OrderReport report = new OrderReport(
                        rs.getString("orderId"),
                        rs.getString("customerId"),
                        rs.getDate("orderDate").toLocalDate(),
                        rs.getString("itemId"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getDouble("total")
                );
                reportData.add(report);
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load report data.").showAndWait();
        }
    }

    public void handlePreviewReport(ActionEvent actionEvent) {
    }

    public void handleDownloadPDF(ActionEvent actionEvent) {
    }

    public void handlePrintInvoice(ActionEvent actionEvent) {
    }
}
