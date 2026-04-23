package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class AdminController {
    @FXML private TableView<MenuItem> table;
    @FXML private TableColumn<MenuItem, Integer> idCol;
    @FXML private TableColumn<MenuItem, String> nameCol;
    @FXML private TableColumn<MenuItem, Double> priceCol;
    @FXML private TableColumn<MenuItem, Integer> stockCol;
    @FXML private TableColumn<MenuItem, String> categoryCol;
    @FXML private TableColumn<MenuItem, Boolean> activeCol;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private TextField categoryField;
    @FXML private TextArea descField;
    @FXML private Label statusLabel;
    private User currentUser;
    private MenuService menuService = new MenuService();
    // setter
    public void setUser(User user) {
        this.currentUser = user;
    }
    // refresh table
    private void refreshTable() {
        table.getItems().setAll(menuService.getAllItems());
    }
    // during update, it auto fills from items
    private void fillForm(MenuItem item) {

        nameField.setText(item.getName());
        priceField.setText(String.valueOf(item.getPrice()));
        stockField.setText(String.valueOf(item.getStock()));
        categoryField.setText(item.getCategory());
        descField.setText(item.getDescription());
    }
    // making a table
    @FXML
    public void initialize() {

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldItem, selected) -> {

                    if (selected != null) {
                        fillForm(selected);
                    }
                }
        );
        // graying out when not active, orange when no item
        table.setRowFactory(tv -> new TableRow<MenuItem>() {
            @Override
            protected void updateItem(MenuItem item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setStyle("");
                }
                else if (!item.isActive() && item.getStock() == 0) {
                    setStyle("-fx-background-color: #d6b3b3;");
                }
                else if (!item.isActive()) {
                    setStyle("-fx-background-color: #cfcfcf;");
                }
                else if (item.getStock() == 0) {
                    setStyle("-fx-background-color: #ffcc80;");
                }
                else {
                    setStyle("");
                }
            }
        });
        refreshTable();
    }
    // back button
    @FXML
    private void goBack() {
        MenuController c = SceneSwitcher.switchScene(table, "Menu.fxml");
        c.setUser(currentUser);
    }
    // clearing fields for adding
    private void clearForm() {
        nameField.clear();
        priceField.clear();
        stockField.clear();
        categoryField.clear();
        descField.clear();
    }
    // add button
    @FXML
    private void handleAdd() {

        if (!isValidInput()) return;
        MenuItem item = new MenuItem();

        item.setName(nameField.getText());
        item.setPrice(Double.parseDouble(priceField.getText()));
        item.setStock(Integer.parseInt(stockField.getText()));
        item.setCategory(categoryField.getText());
        item.setDescription(descField.getText());
        item.setActive(true);

        boolean success = menuService.addItem(item);

        if (success) {
            refreshTable();
            clearForm();
            setStatus("Added successfully.", "green");
        } else {
            setStatus("Add failed.", "red");
        }
    }
    // update button
    @FXML
    private void handleUpdate() {

        if (!isValidInput()) return;
        MenuItem selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            setStatus("Select an item first.", "orange");
            return;
        }

        selected.setName(nameField.getText());
        selected.setPrice(Double.parseDouble(priceField.getText()));
        selected.setStock(Integer.parseInt(stockField.getText()));
        selected.setCategory(categoryField.getText());
        selected.setDescription(descField.getText());

        boolean success = menuService.updateItem(selected);

        if (success) {
            refreshTable();
            setStatus("Updated successfully.", "green");
        } else {
            setStatus("Update failed.", "red");
        }
    }
    // soft delete button
    @FXML
    private void handleDelete() {

        MenuItem selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            setStatus("Select an item first.", "orange");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deactivation");
        alert.setHeaderText("Deactivate this item?");
        alert.setContentText(selected.getName());

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = menuService.deleteItem(selected.getId());

            if (success) {
                refreshTable();
                clearForm();
                setStatus("Deleted successfully.", "green");
            }
        }
    }
    // hard delete button
    @FXML
    private void handleHardDelete() {

        MenuItem selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            setStatus("Select an item first.", "orange");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete permanently?");
        alert.setContentText(selected.getName());

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            boolean success = menuService.hardDeleteItem(selected.getId());

            if (success) {
                refreshTable();
                clearForm();
                setStatus("Completely deleted successfully.", "green");
            }
        }
    }
    // item active button
    @FXML
    private void handleActivate() {

        MenuItem selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            setStatus("Select an item first.", "orange");
            return;
        }

        boolean success = menuService.activateItem(selected.getId());

        if (success) {
            refreshTable();
            clearForm();
            setStatus("Activated successfully.", "green");
        }
    }
    // label confirmation
    private void setStatus(String msg, String color) {
        statusLabel.setText(msg);
        statusLabel.setStyle("-fx-text-fill: " + color + ";");
    }
    // to go dashboard (charts)
    @FXML
    private void goDashboard() {
        DashboardController c = SceneSwitcher.switchScene(table, "Dashboard.fxml");
        c.setUser(currentUser);
    }
    // adding option for each item
    @FXML
    private void handleOptions() {

        MenuItem selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            setStatus("Select an item first.", "orange");
            return;
        }

        try {
            OptionAdminController c = SceneSwitcher.switchScene(table, "OptionAdmin.fxml");
            c.setMenuItem(selected);
            c.setUser(currentUser);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // to stop blank inputs
    private boolean isValidInput() {

        if (nameField.getText().isBlank()) {
            setStatus("Name is required.", "red");
            return false;
        }

        if (categoryField.getText().isBlank()) {
            setStatus("Category is required.", "red");
            return false;
        }

        if (descField.getText().isBlank()) {
            setStatus("Description is required.", "red");
            return false;
        }

        try {
            double price = Double.parseDouble(priceField.getText());
            if (price <= 0) {
                setStatus("Price must be more than 0.", "red");
                return false;
            }

        } catch (Exception e) {
            setStatus("Invalid price.", "red");
            return false;
        }

        try {
            int stock = Integer.parseInt(stockField.getText());
            if (stock < 0) {
                setStatus("Stock cannot be negative.", "red");
                return false;
            }

        } catch (Exception e) {
            setStatus("Invalid stock.", "red");
            return false;
        }
        return true;
    }
}