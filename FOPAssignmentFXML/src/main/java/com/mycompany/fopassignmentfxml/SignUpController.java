package com.mycompany.fopassignmentfxml;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignUpController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private void handleSignUp(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validation checks
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "All fields must be filled.", Alert.AlertType.ERROR);
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Error", "Invalid email format.", Alert.AlertType.ERROR);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match.", Alert.AlertType.ERROR);
            return;
        }

        if (!isValidPassword(password)) {
            showAlert("Error", "Password must be at least 8 characters long, include at least one uppercase letter and one number.", Alert.AlertType.ERROR);
            return;
        }

        // Create new User object
        User user = new User(username, email, password);

        try {
            // Save user data
            saveUserData(user);
            
            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sign-Up Success");
            alert.setHeaderText(null);
            alert.setContentText("You have signed up successfully!");

            // Navigate to LoginPage after closing the alert
            alert.setOnCloseRequest(e -> {
                try {
                    App.setRoot("LoginPage");  // Navigate to LoginPage
                } catch (IOException ex) {
                    ex.printStackTrace();
                    showAlert("Error", "Failed to load the login page.", Alert.AlertType.ERROR);
                }
            });

            alert.showAndWait();  // Show the alert and wait for user to close it

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save user data.", Alert.AlertType.ERROR);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        // Password should be at least 8 characters, with at least one uppercase letter and one number
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return password.matches(passwordRegex);
    }

    private void saveUserData(User user) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("user_data.json");
        List<User> users;

        if (file.exists() && file.length() > 0) {
            users = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
        } else {
            users = new ArrayList<>();
        }

        users.add(user);
        objectMapper.writeValue(file, users);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
