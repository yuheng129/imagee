package com.mycompany.fopassignmentfxml;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LoginController {

    @FXML
    private TextField usernameOrEmailField;

    @FXML
    private PasswordField passwordField;
    

    @FXML
    private void handleLogin(ActionEvent event) {
        String usernameOrEmail = usernameOrEmailField.getText();
        String password = passwordField.getText();
    
        // Validate input fields
        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields must be filled.", Alert.AlertType.ERROR, false);
            return;
        }
    
        try {
            User loggedInUser = getValidUser(usernameOrEmail, password);
            if (loggedInUser != null) {
                // Save the current user information in UserSession
                UserSession.setCurrentUser(loggedInUser);
    
                // Show success alert and navigate to HomePage only on confirmation
                showAlert("Success", "Login successful!", Alert.AlertType.INFORMATION, true);
            } else {
                showAlert("Error", "Wrong Username/Email or password", Alert.AlertType.ERROR, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to read user data.", Alert.AlertType.ERROR, false);
        }
    }

    

    @FXML
    public void signUpScene(@SuppressWarnings("exports") MouseEvent event) {
        try {
            // Navigate to the SignUpPage FXML scene
            App.setRoot("SignUpPage"); // Assuming "SignUpPage" is the name of your FXML file for the SignUp page
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Sign-up page.", Alert.AlertType.ERROR, false);
        }
    }

    private User getValidUser(String usernameOrEmail, String password) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("user_data.json");

        if (!file.exists() || file.length() == 0) {
            return null;
        }

        List<User> users = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));

        for (User user : users) {
            if ((user.getUsername().equals(usernameOrEmail) || user.getEmail().equals(usernameOrEmail))
                    && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

    @FXML
    private void showAlert(String title, String message, Alert.AlertType alertType, boolean navigateToHomePage) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
    
        // Show alert and handle button response
        alert.showAndWait().ifPresent(response -> {
            if (navigateToHomePage && response == ButtonType.OK) {
                navigateToHomePage();
            }
        });
    }

    // Navigate to the HomePage after successful login
    private void navigateToHomePage() {
        try {
            App.setRoot("HomePage");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Home Page.", Alert.AlertType.ERROR, false);
        }
    }
}
