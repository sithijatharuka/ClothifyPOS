package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    @FXML
    private StackPane contentPane; // This matches the fx:id in your FXML

    @FXML
    private Button btnLogout;


    public void showPlaceOrder(ActionEvent actionEvent) {
        loadView("/view/place_order_screen.fxml");
    }

    public void showCustomerManagement(ActionEvent actionEvent) {
        loadView("/view/customer_form.fxml");

    }

    public void showSupplierManagement(ActionEvent actionEvent) {
        loadView("/view/supplier_management_screen.fxml");
    }



    public void showReportManager(ActionEvent actionEvent) {
        loadView("/view/report_manager.fxml");
    }

    public void logout(ActionEvent actionEvent) {
    }

    private void loadView(String fxmlPath) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            // Clear the content pane and add the new view
            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle error (maybe show an alert)
        }
    }

    public void initialize() {
        // Automatically load the Place Order screen when dashboard loads
        loadView("/view/place_order_screen.fxml");
    }

    public void showAddItemScreen(ActionEvent actionEvent) {
        loadView("/view/product_management_screen.fxml");
    }

    public void showOrderHistory(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/order_history.fxml"));
            Parent view = loader.load();

            // Clear the content pane and add the new view
            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleLogout(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/view/login_screen.fxml")); // Adjust path as needed
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Clothify POS - Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load login screen.").showAndWait();
        }
    }
}
