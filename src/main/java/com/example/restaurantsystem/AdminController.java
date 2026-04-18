package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private User currentUser;
    private MenuService menuService = new MenuService();
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
        refreshTable();
    }
    // back button
    @FXML
    private void goBack() {
        MenuController c = SceneSwitcher.switchScene(table, "Menu.fxml");
        c.setUser(currentUser);
    }
    // add button
    @FXML
    private void handleAdd() {
        System.out.println("Add clicked");
    }
    // update button
    @FXML
    private void handleUpdate() {

        MenuItem selected =
                table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            System.out.println("Select an item first");
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
            System.out.println("Updated");
        } else {
            System.out.println("Update failed");
        }
    }
    // soft delete button
    @FXML
    private void handleDelete() {
        System.out.println("Soft delete clicked");
    }
    // hard delete button
    @FXML
    private void handleHardDelete() {
        System.out.println("Hard delete clicked");
    }
}