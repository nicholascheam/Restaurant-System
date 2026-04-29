package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
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
        name.getStyleClass().add("subtitle-label");

        Label price = new Label(String.format("$%.2f", item.getPrice()));
        Label stockLabel = new Label("Stock: " + item.getStock());

        Label desc = new Label(item.getDescription());
        desc.setWrapText(true);
        desc.setMaxWidth(180);
        desc.setTextAlignment(TextAlignment.CENTER);
        desc.setAlignment(Pos.CENTER);

        Button toggleBtn = new Button("Add ▼");
        toggleBtn.getStyleClass().add("green-btn");
        toggleBtn.setMaxWidth(Double.MAX_VALUE);

        if (user.isAdmin()) {
            toggleBtn.setDisable(true);
        }

        if (item.getStock() <= 0) {
            toggleBtn.setDisable(true);
            toggleBtn.setText("Out of Stock");
        }

        VBox expandBox = new VBox(10);
        expandBox.getStyleClass().add("option-group");
        expandBox.setVisible(false);
        expandBox.setManaged(false);
        expandBox.setPrefWidth(190);
        expandBox.setMaxWidth(190);

        int maxStock = Math.max(1, item.getStock());

        Spinner<Integer> qtySpinner = new Spinner<>(1, maxStock, 1);
        qtySpinner.setEditable(true);
        qtySpinner.setMaxWidth(Double.MAX_VALUE);

        VBox optionsBox = new VBox(8);

        List<ItemOption> options = optionService.getOptions(item.getId());

        List<CheckBox> checkBoxes = new ArrayList<>();
        List<ToggleGroup> radioGroups = new ArrayList<>();

        for (ItemOption opt : options) {

            Label groupLabel = new Label(opt.getOptionName());
            groupLabel.setStyle("-fx-font-weight: bold;");
            optionsBox.getChildren().add(groupLabel);

            String[] values = opt.getOptionValues().split(",");

            if (opt.getControlType().equalsIgnoreCase("radio")) {

                ToggleGroup group = new ToggleGroup();
                radioGroups.add(group);

                for (String val : values) {
                    RadioButton rb = new RadioButton(val.trim());
                    rb.setToggleGroup(group);
                    optionsBox.getChildren().add(rb);
                }

            } else if (opt.getControlType().equalsIgnoreCase("checkbox")) {

                for (String val : values) {
                    CheckBox cb = new CheckBox(val.trim());
                    checkBoxes.add(cb); // FIX HERE
                    optionsBox.getChildren().add(cb);
                }
            }
        }

        Button confirmBtn = new Button("Add To Cart");
        confirmBtn.getStyleClass().add("blue-btn");
        confirmBtn.setMinWidth(160);
        confirmBtn.setPrefWidth(160);
        confirmBtn.setPrefHeight(40);

        confirmBtn.setOnAction(e -> {

            int qty = qtySpinner.getValue();
            StringBuilder selected = new StringBuilder();

            // checkboxes
            for (CheckBox cb : checkBoxes) {
                if (cb.isSelected()) {
                    selected.append(cb.getText()).append(", ");
                }
            }

            // radios
            for (ToggleGroup tg : radioGroups) {
                if (tg.getSelectedToggle() != null) {
                    RadioButton rb = (RadioButton) tg.getSelectedToggle();
                    selected.append(rb.getText()).append(", ");
                }
            }

            String custom = selected.toString();

            if (custom.endsWith(", ")) {
                custom = custom.substring(0, custom.length() - 2);
            }

            try {
                order.addItem(item, qty, custom);

                updateCartUI();

                expandBox.setVisible(false);
                expandBox.setManaged(false);
                toggleBtn.setText("Add ▼");

            } catch (Exception ex) {
                AlertUtil.info(ex.getMessage());
            }
        });

        expandBox.getChildren().addAll(
                new Label("Quantity"),
                qtySpinner
        );

        if (!options.isEmpty()) {
            expandBox.getChildren().addAll(
                    new Label("Options"),
                    optionsBox
            );
        }

        expandBox.getChildren().add(confirmBtn);

        toggleBtn.setOnAction(e -> {

            boolean show = !expandBox.isVisible();

            expandBox.setVisible(show);
            expandBox.setManaged(show);

            toggleBtn.setText(show ? "Add ▲" : "Add ▼");
        });

        VBox box = new VBox(10, name, price, stockLabel, desc, toggleBtn, expandBox);

        box.getStyleClass().add("menu-card");
        box.setPrefWidth(220);
        box.setMinHeight(300);
        box.setAlignment(Pos.TOP_CENTER);

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
            label.setWrapText(false);
            HBox.setHgrow(label, Priority.ALWAYS);
            Button minusBtn = new Button("-1");
            Button removeAllBtn = new Button("Remove All");
            minusBtn.getStyleClass().add("blue-btn");
            removeAllBtn.getStyleClass().add("red-btn");
            minusBtn.setOnAction(e -> {
                try {
                    order.removeItem(oi.getMenuItem(), 1);
                    updateCartUI();
                } catch (Exception ex) {
                    AlertUtil.info(ex.getMessage());
                }
            });

            removeAllBtn.setOnAction(e -> {
                try {
                    order.removeItem(oi.getMenuItem(), oi.getQuantity());
                    updateCartUI();
                } catch (Exception ex) {
                    AlertUtil.info(ex.getMessage());
                }
            });

            HBox row = new HBox(10, label, minusBtn, removeAllBtn);
            row.getStyleClass().add("cart-row");
            row.setFillHeight(true);
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

                try {
                    order.addItem(item, qty, customText);
                    updateCartUI();
                } catch (Exception ex) {
                    AlertUtil.info(ex.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
