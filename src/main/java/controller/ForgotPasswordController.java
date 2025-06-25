package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgotPasswordController {
    public Hyperlink linktoBack;

    public void back(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) linktoBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/login_screen.fxml")); // Adjust path as needed
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Clothify POS - Login");
        stage.show();
    }
}
