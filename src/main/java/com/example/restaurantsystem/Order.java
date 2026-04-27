package com.example.restaurantsystem;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {
    int id;
    User user;
    LocalDateTime dateTime;
    ArrayList<OrderItem> items;
    // getters, setters in "Order" method
    User getUser() {
        return user;
    }
    ArrayList<OrderItem> getItems() {
        return items;
    }
    LocalDateTime getDateTime() {
        return dateTime;
    }

    Order(User user){
        this.user = user;
        this.items = new ArrayList<>();
        this.dateTime = LocalDateTime.now();
    }
    // method overloading for addItem
    void addItem(MenuItem menuItem, int quantity) {
        addItem(menuItem, quantity, "");
    }

    void addItem(MenuItem menuItem, int quantity, String customText) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        if (!menuItem.isAvailable()) {
            throw new IllegalArgumentException("Item not available.");
        }

        if (menuItem.getStock() < quantity) {
            throw new IllegalArgumentException(
                    "Only " + menuItem.getStock() + " left in stock."
            );
        }

        for (OrderItem oi : items) {

            boolean sameItem = oi.getMenuItem().getId() == menuItem.getId();

            boolean sameCustom = oi.getCustomText().equals(customText);

            if (sameItem && sameCustom) {

                int totalQuantity = oi.getQuantity() + quantity;

                if (totalQuantity > menuItem.getStock()) {
                    throw new IllegalArgumentException(
                            "Cannot add more. Max stock: " + menuItem.getStock()
                    );
                }

                oi.setQuantity(totalQuantity);
                return;
            }
        }

        items.add(new OrderItem(menuItem, quantity, customText));
    }

    void removeItem(MenuItem menuItem, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        for (int i = 0; i < items.size(); i++) {

            OrderItem oi = items.get(i);
            if (oi.getMenuItem().getId() == menuItem.getId()) {
                if (quantity > oi.getQuantity()) {
                    throw new IllegalArgumentException(
                            "Cannot remove more than existing quantity."
                    );
                }

                int newQuantity = oi.getQuantity() - quantity;

                if (newQuantity == 0) {
                    items.remove(i);
                } else {
                    oi.setQuantity(newQuantity);
                }
                return;
            }
        }
        throw new IllegalArgumentException("Item not found in cart.");
    }
    // iterate through orderitem list and add up everything
    double calculateTotal() {
        double total = 0;

        for (OrderItem oi : items) {
            total += oi.getSubtotal();
        }

        return total;
    }
    // for clearing the cart
    void clear() {
        items.clear();
    }
}
