package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class PaymentController {

    @FXML private RadioButton cashRadio;
    @FXML private RadioButton cardRadio;
    @FXML private TextField cardField;
    @FXML private Label statusLabel;

    private Stage stage;
    private boolean paid = false;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isPaid() {
        return paid;
    }
    // initialize the page
    @FXML
    public void initialize() {

        ToggleGroup group = new ToggleGroup();

        cashRadio.setToggleGroup(group);
        cardRadio.setToggleGroup(group);

        cashRadio.setSelected(true);

        cardField.setDisable(true);

        group.selectedToggleProperty().addListener((obs, oldV, newV) -> {

            if (cardRadio.isSelected()) {
                cardField.setDisable(false);
            } else {
                cardField.clear();
                cardField.setDisable(true);
            }

            statusLabel.setText("");
        });

        // only digits
        cardField.textProperty().addListener((obs, oldV, newV) -> {

            if (!newV.matches("\\d*")) {
                cardField.setText(newV.replaceAll("[^\\d]", ""));
            }

            if (cardField.getText().length() > 16) {
                cardField.setText(
                        cardField.getText().substring(0, 16)
                );
            }
        });
    }
    // pay button, validating card/cash options
    @FXML
    private void handlePay() {

        if (cashRadio.isSelected()) {
            paid = true;
            stage.close();
            return;
        }

        if (cardRadio.isSelected()) {

            String card = cardField.getText().trim();

            if (card.length() != 16) {
                statusLabel.setText("Card must be exactly 16 digits.");
                statusLabel.getStyleClass().setAll("error-text");
                return;
            }

            paid = true;
            stage.close();
        }
    }
    // cancel button
    @FXML
    private void handleCancel() {
        paid = false;
        stage.close();
    }
}
