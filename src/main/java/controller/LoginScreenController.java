package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginScreenController {

    public TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;
    @FXML private Hyperlink linkForgotPassword;
    @FXML private Hyperlink linkRegister;

    @FXML
    public void initialize() {
        btnLogin.setOnAction(this::handleLogin);
//        linkRegister.setOnAction(this::handleRegister);
     //   linkForgotPassword.setOnAction(this::handleForgotPassword);
    }

    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // ✅ Load dashboard after successful login
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard_screen.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) btnLogin.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Dashboard");
                stage.show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid username or password!").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Login failed due to system error!").show();
        }
    }

//    private void handleRegister(ActionEvent event) {
//        try {
//            // Load the sign-up FXML file
//            Parent signUpRoot = FXMLLoader.load(getClass().getResource("/view/signup.fxml"));
//
//            // Get the current stage
//            Stage stage = (Stage) linkRegister.getScene().getWindow();
//
//            // Set the new scene (sign up page)
//            stage.setScene(new Scene(signUpRoot));
//            stage.setTitle("Sign Up - Clothify POS");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            // You may want to show an alert here
//        }
//    }

    @FXML
    private void goToSignUpPage(ActionEvent event) throws IOException {
        Stage stage = (Stage) linkRegister.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/signup.fxml")); // signup page
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Clothify POS - Register");
        stage.show();
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) throws IOException {
        Stage stage = (Stage) linkRegister.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/forgot_password.fxml")); // signup page
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Clothify POS - Register");
        stage.show();
    }
}
