package com.example.restaurantsystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneSwitcher {

    public static <T> T switchScene(Node node, String fxml) {

        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxml));

            Parent root = loader.load();

            Stage stage = (Stage) node.getScene().getWindow();

            double width = stage.getWidth();
            double height = stage.getHeight();
            boolean maximized = stage.isMaximized();

            Scene scene = new Scene(root, width, height);

            scene.getStylesheets().add(SceneSwitcher.class.getResource("style.css").toExternalForm());

            stage.setScene(scene);
            stage.setMaximized(maximized);

            return loader.getController();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}