import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Starter extends Application {
    public static void main(String[] args) {
        launch();
    }

    public void start(Stage stage) throws Exception{
        // Load FXML
        Parent root = FXMLLoader.load(getClass().getResource("/view/login_screen.fxml"));

        // Create scene with default size
        Scene scene = new Scene(root, 900, 700);

        // Set window properties
        stage.setScene(scene);
        stage.setTitle("Clothify POS System");  // Set window title
        stage.setMinWidth(800);  // Minimum width
        stage.setMinHeight(500); // Minimum height

        // Optional: Center window on screen
        stage.centerOnScreen();

        stage.show();

    }
}
