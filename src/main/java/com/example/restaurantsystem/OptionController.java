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
    private boolean confirmed = false;
    // getter and setters
    public String getSelectedText() {
        return selectedText;
    }
    public int getSelectedQty() {
        return qtySpinner.getValue();
    }
    public boolean isConfirmed() {
        return confirmed;
    }
    public void setData(MenuItem item, List<ItemOption> options) {

        this.item = item;
        titleLabel.setText(item.getName());
        qtySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1));
        qtySpinner.valueProperty().addListener((a,b,c)->updateTotal());
        optionsBox.getChildren().clear();

        for (ItemOption op : options) {
            Label label = new Label(op.getOptionName());
            optionsBox.getChildren().add(label);
            String[] vals = op.getOptionValues().split(",");

            if (op.getControlType().equalsIgnoreCase("radio")) {
                ToggleGroup group = new ToggleGroup();
                VBox radioBox = new VBox(5);

                for (String val : vals) {
                    RadioButton rb = new RadioButton(val.trim());
                    rb.setToggleGroup(group);
                    radioBox.getChildren().add(rb);
                }
                ((RadioButton) radioBox.getChildren().get(0)).setSelected(true);
                optionsBox.getChildren().add(radioBox);
            }

            else if (op.getControlType().equalsIgnoreCase("checkbox")) {

                VBox checkBox = new VBox(5);

                for (String val : vals) {
                    CheckBox cb = new CheckBox(val.trim());
                    checkBox.getChildren().add(cb);
                }
                optionsBox.getChildren().add(checkBox);
            }
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
            if (node instanceof VBox box) {
                for (Node inner : box.getChildren()) {
                    if (inner instanceof RadioButton rb && rb.isSelected()) {
                        if (!result.isEmpty()) result.append(", ");
                        result.append(rb.getText());
                    }
                    if (inner instanceof CheckBox cb && cb.isSelected()) {
                        if (!result.isEmpty()) result.append(", ");
                        result.append(cb.getText());
                    }
                }
            }
        }
        if (!notesField.getText().isBlank()) {
            if (!result.isEmpty()) result.append(", ");
            result.append("Note: ").append(notesField.getText());
        }
        selectedText = result.toString();
        confirmed = true;
        stage.close();
    }
    // closing order window
    @FXML
    private void handleCancel() {
        confirmed = false;
        stage.close();
    }
}