package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class OptionController {

    @FXML private Label titleLabel;
    @FXML private VBox optionsBox;
    @FXML private TextField notesField;
    @FXML private Spinner<Integer> qtySpinner;
    @FXML private Label totalLabel;

    private MenuItem item;
    private Order order;
    private Stage stage;
    private String selectedText = "";
    // getter and setters
    public String getSelectedText() {
        return selectedText;
    }

    public void setData(MenuItem item, List<ItemOption> options) {

        this.item = item;
        titleLabel.setText(item.getName());
        qtySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1));
        qtySpinner.valueProperty().addListener((a,b,c)->updateTotal());

        for (ItemOption op : options) {

            Label label = new Label(op.getOptionName());
            ComboBox<String> combo = new ComboBox<>();
            String[] vals = op.getOptionValues().split(",");
            combo.getItems().addAll(vals);

            if (vals.length > 0) combo.setValue(vals[0].trim());
            optionsBox.getChildren().addAll(label, combo);
        }
        updateTotal();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    // initialize the option
    @FXML
    public void initialize() {

    }
    // update the total price
    private void updateTotal() {

        if (item == null) return;
        int qty = qtySpinner.getValue();
        double total = item.getPrice() * qty;

        totalLabel.setText(String.format("Total: $%.2f", total));
    }
    // adding quantity handling
    @FXML
    private void handleAdd() {

        StringBuilder result = new StringBuilder();

        for (Node node : optionsBox.getChildren()) {
            if (node instanceof ComboBox<?> combo) {
                Object selected = combo.getValue();

                if (selected != null) {
                    if (!result.isEmpty()) result.append(", ");
                    result.append(selected.toString());
                }
            }
        }

        if (!notesField.getText().isBlank()) {
            if (!result.isEmpty()) result.append(", ");
            result.append("Notes: ").append(notesField.getText());
        }

        selectedText = result.toString();
        stage.close();
    }
    // closing order window
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }
}