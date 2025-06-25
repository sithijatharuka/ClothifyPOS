package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SignUpController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private Button btnSignUp;

    @FXML
    private Hyperlink linkBackToLogin;

    // Called when user clicks the Sign Up button
    @FXML
    public void signUp(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Passwords do not match.");
            return;
        }

        // Save user to database
        try {
            Connection con = DBConnection.getInstance().getConnection();
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);  // Remember to hash passwords in production!

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "User registered successfully.");
                clearFields();
                goToLoginPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to register user.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Database error: " + e.getMessage());
        }
    }

    // Called when user clicks "Back to Login" hyperlink
    @FXML
    public void backToLogin(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnSignUp.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/login_screen.fxml")); // Adjust path as needed
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Clothify POS - Login");
        stage.show();
    }

    private void goToLoginPage() throws IOException {
        Stage stage = (Stage) btnSignUp.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/login_screen.fxml")); // Make sure this path is correct
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Clothify POS - Login");
        stage.show();
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        txtUsername.clear();
        txtPassword.clear();
        txtConfirmPassword.clear();
    }
}
