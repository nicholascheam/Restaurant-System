package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    AuthService authService = new AuthService();
    // method for register
    @FXML
    private void handleRegister() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean success =
                authService.register(username, password, "user");
        // if works, alert pop up saying register successful
        if (success) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Registration successful!");
            alert.showAndWait();

            SceneSwitcher.switchScene(usernameField, "Login.fxml");
        // if not error alert
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Username already exists.");
            alert.showAndWait();
        }
    }
    // go to login page
    @FXML private void goToLogin() {
        SceneSwitcher.switchScene(usernameField, "Login.fxml");
    }
}
