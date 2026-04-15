package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
            System.out.println("Login success");

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml")
                );

                Parent root = loader.load();

                MenuController controller = loader.getController();
                controller.setUser(user);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                Scene scene = new Scene(root, 800, 600);

                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm()
                );

                stage.setScene(scene);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Login failed");
        }
    }
    // register page
    @FXML
    private void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 300));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}