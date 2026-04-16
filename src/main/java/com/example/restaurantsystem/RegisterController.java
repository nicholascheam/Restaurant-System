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

    @FXML
    private void handleRegister() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean success =
                authService.register(username, password, "user");

        if (success) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Registration successful!");
            alert.showAndWait();

            SceneSwitcher.switchScene(usernameField, "Login.fxml");

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Username already exists.");
            alert.showAndWait();
        }
    }
}
