package com.example.restaurantsystem;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {
    int id;
    User user;
    LocalDateTime dateTime;
    ArrayList<OrderItem> items;

    void totalPrice(){

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
    void removeItem(MenuItem menuItem, int quantity){

    }
    void calculateTotal(){

    }
}
