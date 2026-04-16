package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private AuthService authService = new AuthService();
    // login transitioning into menu page
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = authService.login(username, password);

        if (user != null) {

            MenuController controller =
                    SceneSwitcher.switchScene(usernameField, "Menu.fxml");

            if (controller != null) {
                controller.setUser(user);
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password.");
            alert.showAndWait();
        }
    }
    // register page
    @FXML
    private void goToRegister() {
        SceneSwitcher.switchScene(usernameField, "Register.fxml");
    }
}