package com.example.restaurantsystem;

import javafx.scene.control.Alert;

public class AlertUtil {

    public static void info(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void error(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void warn(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}