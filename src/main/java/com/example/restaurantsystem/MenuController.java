package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    @FXML
    private Label userLabel;

    public void setUser(User user) {
        this.user = user;
        this.order = new Order(user);

        userLabel.setText("Hello, " + user.getUsername());

        loadMenuItems();
    }
    private List<MenuItem> getMenuItems() {
        List<MenuItem> list = new ArrayList<>();

        MenuItem m1 = new MenuItem();
        m1.setId(1);
        m1.setName("Burger");
        m1.setPrice(5.0);
        m1.setStock(10);

        MenuItem m2 = new MenuItem();
        m2.setId(2);
        m2.setName("Fries");
        m2.setPrice(3.0);
        m2.setStock(10);

        list.add(m1);
        list.add(m2);

        return list;
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

        addBtn.setOnAction(e -> {
            order.addItem(item, 1);
            System.out.println("Added: " + item.getName());
        });

        VBox box = new VBox(10, name, price, addBtn);
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

    @FXML
    private void handlePlaceOrder() {

        if (order.getItems().isEmpty()) {
            System.out.println("Cart is empty");
            return;
        }

        orderService.saveOrder(order);

        System.out.println("Order placed!");

        order.clear();
        updateCartUI();
    }
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("Login.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) menuGrid.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 300));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
