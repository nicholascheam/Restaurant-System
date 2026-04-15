package com.example.restaurantsystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneSwitcher {

    public static <T> T switchScene(Node node, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneSwitcher.class.getResource(fxml)
            );

            Parent root = loader.load();

            Stage stage = (Stage) node.getScene().getWindow();

            Scene scene = new Scene(root, 800, 600);

            scene.getStylesheets().add(SceneSwitcher.class.getResource("style.css").toExternalForm()
            );

            stage.setScene(scene);

            return loader.getController();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
