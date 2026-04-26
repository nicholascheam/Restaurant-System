package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private OptionService optionService = new OptionService();
    @FXML
    private Label userLabel;
    @FXML
    private Button adminButton;
    @FXML
    private VBox categoryBox;

    public void setUser(User user) {
        this.user = user;
        if (user.isAdmin()) {
            totalLabel.setText("Admin mode - ordering disabled");
        }
        this.order = new Order(user);

        userLabel.setText("Hello, " + user.getUsername());
        userLabel.getStyleClass().add("title-label");
        adminButton.setVisible(user.isAdmin());
        adminButton.setManaged(user.isAdmin());
        loadCategories();
        loadMenuItems();
    }

    // loading categories
    private void loadCategories() {

        categoryBox.getChildren().clear();

        categoryBox.getChildren().add(new Label("Categories"));

        Button allBtn = new Button("All");
        allBtn.setMaxWidth(Double.MAX_VALUE);
        allBtn.setOnAction(e -> loadMenuItems());
        allBtn.getStyleClass().add("blue-btn");
        categoryBox.getChildren().add(allBtn);

        Map<String, Integer> counts = menuService.getCategoryCounts();

        List<String> categories = new ArrayList<>(counts.keySet());

        categories.sort((a, b) -> {

            int countA = counts.get(a);
            int countB = counts.get(b);

            if (countA == 0 && countB > 0) return 1;
            if (countA > 0 && countB == 0) return -1;

            return a.compareToIgnoreCase(b);
        });

        for (String cat : categories) {

            int count = counts.get(cat);

            Button btn = new Button(cat + " (" + count + ")");
            btn.getStyleClass().add("blue-btn");

            btn.setMaxWidth(Double.MAX_VALUE);

            if (count == 0) {
                btn.setDisable(true);
            } else {
                btn.setOnAction(e -> loadMenuItemsByCategory(cat));
            }

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
        menuGrid.getChildren().clear();
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
        addBtn.getStyleClass().add("green-btn");
        if (user.isAdmin()) {
            addBtn.setDisable(true);
        }
        if (item.getStock() <= 0) {
            addBtn.setDisable(true);
            addBtn.setText("Out of Stock");
        }
        Label stockLabel = new Label("Stock: " + item.getStock());
        Label desc = new Label(item.getDescription());
        desc.setWrapText(true);
        addBtn.setOnAction(e -> handleAddItem(item));

        Button infoBtn = new Button("More Info");
        infoBtn.getStyleClass().add("blue-btn");

        infoBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(item.getName());
            alert.setHeaderText(item.getName());
            alert.setContentText(item.getDescription());
            alert.showAndWait();
        });
        VBox box = new VBox(10, name, price, stockLabel, addBtn, infoBtn);
        box.getStyleClass().add("menu-card");
        box.setPrefWidth(180);
        box.setMinHeight(220);

        return box;
    }

    // for deleting cart after ordering
    private void updateCartUI() {

        cartBox.getChildren().clear();
        for (OrderItem oi : order.getItems()) {

            String text = oi.getMenuItem().getName();
            if (oi.getCustomText() != null && !oi.getCustomText().isBlank()) {
                text += " (" + oi.getCustomText() + ")";
            }
            text += " x" + oi.getQuantity();

            Label label = new Label(text);
            Button minusBtn = new Button("-1");
            Button removeAllBtn = new Button("Remove All");
            minusBtn.getStyleClass().add("blue-btn");
            removeAllBtn.getStyleClass().add("red-btn");
            minusBtn.setOnAction(e -> {
                order.removeItem(oi.getMenuItem(), 1);
                updateCartUI();
            });

            removeAllBtn.setOnAction(e -> {
                order.removeItem(oi.getMenuItem(), oi.getQuantity());
                updateCartUI();
            });

            HBox row = new HBox(10, label, minusBtn, removeAllBtn);
            cartBox.getChildren().add(row);
            cartBox.getStyleClass().add("cart-box");
        }

        totalLabel.setText(
                String.format("Total: $%.2f", order.calculateTotal())
        );
    }

    // "Place Order" button
    @FXML
    private void handlePlaceOrder() {

        if (user.isAdmin()) {
            AlertUtil.info("Admins cannot place customer orders.");
            return;
        }

        if (order.getItems().isEmpty()) {
            AlertUtil.info("Cart is empty.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Payment.fxml"));

            Parent root = loader.load();

            PaymentController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Payment");
            stage.setScene(new Scene(root));
            stage.setResizable(false);

            controller.setStage(stage);

            stage.showAndWait();

            if (!controller.isPaid()) {
                return;
            }

            boolean success = orderService.placeOrder(order);

            if (success) {
                AlertUtil.info("Payment successful. Order placed!");
                order.clear();
                updateCartUI();
                loadMenuItems();
            } else {
                AlertUtil.info("Order failed. Not enough stock.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.info("Payment page failed to open.");
        }
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
        menuGrid.getChildren().clear();
        List<MenuItem> filtered = new ArrayList<>();

        for (MenuItem item : getMenuItems()) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                filtered.add(item);
            }
        }

        loadMenuItems(filtered);
    }
    private void handleAddItem(MenuItem item) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Option.fxml"));

            Parent root = loader.load();

            OptionController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle(item.getName() + " Options");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.sizeToScene();

            controller.setStage(stage);
            controller.setData(item, optionService.getOptions(item.getId()));

            stage.showAndWait();

            if (controller.isConfirmed()) {

                String customText = controller.getSelectedText();
                int qty = controller.getSelectedQty();

                order.addItem(item, qty, customText);
                updateCartUI();
            }

            updateCartUI();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
