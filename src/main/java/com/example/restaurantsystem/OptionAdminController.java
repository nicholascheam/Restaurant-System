package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class OptionAdminController {

    @FXML private TableView<ItemOption> table;

    @FXML private TableColumn<ItemOption, Integer> idCol;
    @FXML private TableColumn<ItemOption, String> nameCol;
    @FXML private TableColumn<ItemOption, String> valuesCol;
    @FXML private TableColumn<ItemOption, String> typeCol;
    @FXML private TableColumn<ItemOption, Boolean> requiredCol;

    @FXML private Label itemLabel;
    @FXML private Label statusLabel;

    @FXML private TextField nameField;
    @FXML private TextField valuesField;

    @FXML private ComboBox<String> typeBox;
    @FXML private CheckBox requiredBox;

    private MenuItem menuItem;
    private User currentUser;

    private OptionService optionService = new OptionService();
    // getter and setters
    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
        itemLabel.setText("Item: " + menuItem.getName());
        refreshTable();
    }

    public void setUser(User user) {
        this.currentUser = user;
    }
    // initializing each column
    @FXML
    public void initialize() {

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("optionName"));
        valuesCol.setCellValueFactory(new PropertyValueFactory<>("optionValues"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("controlType"));
        requiredCol.setCellValueFactory(new PropertyValueFactory<>("required"));

        typeBox.getItems().addAll("radio", "checkbox");

        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldItem, selected) -> {
                    if (selected != null) {
                        fillForm(selected);
                    }
                }
        );
    }
    // refresh table (after every action)
    private void refreshTable() {
        if (menuItem != null) {
            table.getItems().setAll(optionService.getOptions(menuItem.getId()));
        }
    }
    // to retrieve then set the option for each item
    private void fillForm(ItemOption op) {
        nameField.setText(op.getOptionName());
        valuesField.setText(op.getOptionValues());
        typeBox.setValue(op.getControlType());
        requiredBox.setSelected(op.isRequired());
    }
    // adding option
    @FXML
    private void handleAdd() {

        ItemOption op = new ItemOption(0, menuItem.getId(), nameField.getText(), valuesField.getText(), typeBox.getValue(), requiredBox.isSelected());

        if (optionService.addOption(op)) {
            refreshTable();
            statusLabel.setText("Added.");
        }
    }
    // updating option
    @FXML
    private void handleUpdate() {

        ItemOption selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) return;
        ItemOption op = new ItemOption(selected.getId(), menuItem.getId(), nameField.getText(), valuesField.getText(), typeBox.getValue(), requiredBox.isSelected());

        if (optionService.updateOption(op)) {
            refreshTable();
            statusLabel.setText("Updated.");
        }
    }
    // delete option
    @FXML
    private void handleDelete() {

        ItemOption selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        if (optionService.deleteOption(selected.getId())) {
            refreshTable();
            statusLabel.setText("Deleted.");
        }
    }
    // switch back to admin panel
    @FXML
    private void goBack() {
        AdminController c = SceneSwitcher.switchScene(table, "Admin.fxml");

        c.setUser(currentUser);
    }
    // adding option
    public boolean addOption(ItemOption op) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = """
                        INSERT INTO item_options
                        (menu_item_id, option_name, option_values, control_type, required)
                        VALUES (?, ?, ?, ?, ?)
                        """;

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, op.getMenuItemId());
            ps.setString(2, op.getOptionName());
            ps.setString(3, op.getOptionValues());
            ps.setString(4, op.getControlType());
            ps.setBoolean(5, op.isRequired());

            ps.executeUpdate();
            conn.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    // updating option
    public boolean updateOption(ItemOption op) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = """
        UPDATE item_options
        SET option_name=?, option_values=?, control_type=?, required=?
        WHERE id=?
        """;

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, op.getOptionName());
            ps.setString(2, op.getOptionValues());
            ps.setString(3, op.getControlType());
            ps.setBoolean(4, op.isRequired());
            ps.setInt(5, op.getId());

            ps.executeUpdate();
            conn.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    // delete option
    public boolean deleteOption(int id) {
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "DELETE FROM item_options WHERE id=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
            conn.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}