package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class MenuController {

    @FXML
    private GridPane menuGrid;
    @FXML
    private Label totalLabel;
    @FXML
    private VBox cartBox;

    private User user;
    private Order order;
    private OrderService orderService = new OrderService();
    private MenuService menuService = new MenuService();
    @FXML
    private Label userLabel;
    @FXML
    private Button adminButton;
    @FXML
    private VBox categoryBox;

    public void setUser(User user) {
        this.user = user;
        this.order = new Order(user);

        userLabel.setText("Hello, " + user.getUsername());
        adminButton.setVisible(user.isAdmin());
        adminButton.setManaged(user.isAdmin());
        loadCategories();
        loadMenuItems();
    }
    // loading categories
    private void loadCategories() {

        categoryBox.getChildren().clear();

        categoryBox.getChildren().add(
                new Label("Categories")
        );

        Button allBtn = new Button("All");
        allBtn.setOnAction(e -> loadMenuItems());
        categoryBox.getChildren().add(allBtn);

        for (String cat : menuService.getCategories()) {

            Button btn = new Button(cat);

            btn.setMaxWidth(Double.MAX_VALUE);

            btn.setOnAction(e ->
                    loadMenuItemsByCategory(cat)
            );

            categoryBox.getChildren().add(btn);
        }
    }
    private List<MenuItem> getMenuItems() {
        MenuService menuService = new MenuService();
        List<MenuItem> items = menuService.getAllItems();
        return items;
    }
    // retrieve item from menu then put it in a grid
    private void loadMenuItems() {
        List<MenuItem> items = getMenuItems(); // pretend from DB

        int col = 0;
        int row = 0;

        for (MenuItem item : items) {
            VBox card = createItemCard(item);

            menuGrid.add(card, col, row);

            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }
    // puts item in a structured box to be structured like blocks
    private VBox createItemCard(MenuItem item) {

        Label name = new Label(item.getName());
        Label price = new Label("$" + item.getPrice());

        Button addBtn = new Button("Add");
        Label desc = new Label(item.getDescription());
        desc.setWrapText(true);
        addBtn.setOnAction(e -> {
            order.addItem(item, 1);
            updateCartUI();
        });

        Button infoBtn = new Button("More Info");

        infoBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(item.getName());
            alert.setHeaderText(item.getName());
            alert.setContentText(item.getDescription());
            alert.showAndWait();
        });
        VBox box = new VBox(10, name, price, addBtn, infoBtn);
        box.setStyle("-fx-border-color: black; -fx-padding: 10;");

        return box;
    }
    // for deleting cart after ordering
    private void updateCartUI() {

        cartBox.getChildren().clear();

        for (OrderItem oi : order.getItems()) {

            Label label = new Label(
                    oi.getMenuItem().getName() + " x" + oi.getQuantity()
            );

            Button removeBtn = new Button("Remove");

            removeBtn.setOnAction(e -> {
                order.removeItem(oi.getMenuItem(), 1);
                updateCartUI();
            });

            HBox row = new HBox(10, label, removeBtn);

            cartBox.getChildren().add(row);
        }

        totalLabel.setText("Total: $" + order.calculateTotal());
    }
    // alert blueprint for use
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    // "Place Order" button
    @FXML
    private void handlePlaceOrder() {

        if (order.getItems().isEmpty()) {
            showAlert("Cart is empty.");
            return;
        }

        boolean success = orderService.placeOrder(order);

        if (success) {
            showAlert("Order placed!");
            order.clear();
            updateCartUI();
            loadMenuItems();
        } else {
            showAlert("Not enough stock.");
        }

        showAlert("Order placed successfully!");

        order.clear();
        updateCartUI();
    }
    // "Logout" button
    @FXML
    private void handleLogout() {
        SceneSwitcher.switchScene(menuGrid, "Login.fxml");
    }
    // "Admin panel" button
    @FXML
    private void goToAdmin() {
        AdminController c = SceneSwitcher.switchScene(menuGrid, "Admin.fxml");
        c.setUser(user);
    }
    // method for showing each category
    private void loadMenuItems(List<MenuItem> items) {
        menuGrid.getChildren().clear();

        int col = 0;
        int row = 0;

        if (items.isEmpty()) {
            Label empty = new Label("No items found.");
            menuGrid.add(empty, 0, 0);
            return;
        }
        for (MenuItem item : items) {
            VBox card = createItemCard(item);
            menuGrid.add(card, col, row);

            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }
    private void loadMenuItemsByCategory(String category) {

        List<MenuItem> filtered = new ArrayList<>();

        for (MenuItem item : getMenuItems()) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                filtered.add(item);
            }
        }

        loadMenuItems(filtered);
    }
    // each method for each button
    @FXML
    private void showAll() {
        loadMenuItems(getMenuItems());
    }

    @FXML
    private void showDrinks() {
        loadMenuItemsByCategory("Drinks");
    }

    @FXML
    private void showDesserts() {
        loadMenuItemsByCategory("Desserts");
    }

    @FXML
    private void showBurgers() {
        loadMenuItemsByCategory("Burgers");
    }
}
