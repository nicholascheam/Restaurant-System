package com.example.restaurantsystem;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {
    int id;
    User user;
    LocalDateTime dateTime;
    ArrayList<OrderItem> items;

    Order(User user){
        this.user = user;
        this.items = new ArrayList<>();
    }
    void addItem(MenuItem menuItem, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        if (menuItem.getStock() < quantity) {
            System.out.println("Not enough stock");
            return;
        }

        for (OrderItem oi : items) {
            if (oi.getMenuItem().getId() == menuItem.getId()) {

                int totalQuantity = oi.getQuantity() + quantity;

                if (totalQuantity > menuItem.getStock()) {
                    System.out.println("Not enough stock");
                    return;
                }

                oi.setQuantity(totalQuantity);
                return;
            }
        }

        OrderItem newItem = new OrderItem(menuItem, quantity);
        items.add(newItem);
    }
    void removeItem(MenuItem menuItem, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        for (int i = 0; i < items.size(); i++) {
            OrderItem oi = items.get(i);

            if (oi.getMenuItem().getId() == menuItem.getId()) {

                if (quantity > oi.getQuantity()) {
                    System.out.println("Cannot remove more than existing quantity");
                    return;
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

        System.out.println("Item not found in order");
    }
    double calculateTotal() {
        double total = 0;

        for (OrderItem oi : items) {
            total += oi.getSubtotal();
        }

        return total;
    }
}
