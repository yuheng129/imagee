import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    @FXML
    private AnchorPane ScenePane;  // The root AnchorPane

        @FXML
        void logout(MouseEvent event) throws IOException {
            // Load the LoginPage.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
            AnchorPane loginPage = loader.load();  // Load the FXML into a new AnchorPane
        
            // Get the current stage (window)
            Stage stage = (Stage) ScenePane.getScene().getWindow();
        
            // Set the new scene with the login page
            Scene newScene = new Scene(loginPage);
            stage.setScene(newScene);  // Switch the scene to the login page
        
            // Optionally, you can change the stage title as well
            stage.setTitle("Login Page");
        
            // Show the new scene (login page)
            stage.show();
        }
}

