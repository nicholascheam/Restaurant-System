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
            System.out.println("Item not available");
            return;
        }

        if (menuItem.getStock() < quantity) {
            AlertUtil.info("Only " + menuItem.getStock() + " left in stock.");
            return;
        }

        for (OrderItem oi : items) {

            boolean sameItem = oi.getMenuItem().getId() == menuItem.getId();
            boolean sameCustom = oi.getCustomText().equals(customText);

            if (sameItem && sameCustom) {
                int totalQuantity = oi.getQuantity() + quantity;

                if (totalQuantity > menuItem.getStock()) {
                    AlertUtil.info("Cannot add more. Max stock: " + menuItem.getStock());
                    return;
                }

                oi.setQuantity(totalQuantity);
                return;
            }
        }

        OrderItem newItem = new OrderItem(menuItem, quantity, customText);
        items.add(newItem);
    }

    void removeItem(MenuItem menuItem, int quantity) {
        // check for stock vs order quantity
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }
        // use index based iteration for more control
        for (int i = 0; i < items.size(); i++) {
            OrderItem oi = items.get(i);
            // compare exact food item id
            if (oi.getMenuItem().getId() == menuItem.getId()) {
                // check for stock vs quantity
                if (quantity > oi.getQuantity()) {
                    System.out.println("Cannot remove more than existing quantity");
                    return;
                }

                int newQuantity = oi.getQuantity() - quantity;
                // if no more stock, remove the item from order list, else set to new quantity
                if (newQuantity == 0) {
                    items.remove(i);
                } else {
                    oi.setQuantity(newQuantity);
                }

                return;
            }
        }
        // if no items, print out items not found
        System.out.println("Item not found in order");
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
