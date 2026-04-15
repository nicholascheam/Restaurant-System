package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

    public void setUser(User user) {
        this.user = user;
        this.order = new Order(user);

        loadMenuItems();
    }
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
}
