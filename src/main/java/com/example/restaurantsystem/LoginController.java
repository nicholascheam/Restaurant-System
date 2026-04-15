package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = authService.login(username, password);

        if (user != null) {
            System.out.println("Login success");
        } else {
            System.out.println("Login failed");
        }
    }
}