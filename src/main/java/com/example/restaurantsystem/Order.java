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
        // prevent order -1 items
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }
        // check for stock vs order quantity
        if (menuItem.getStock() < quantity) {
            System.out.println("Not enough stock");
            return;
        }
        // iterate through whole items list
        for (OrderItem oi : items) {
            // check for exact id item
            if (oi.getMenuItem().getId() == menuItem.getId()) {

                int totalQuantity = oi.getQuantity() + quantity;
                // check for stock vs order quantity
                if (totalQuantity > menuItem.getStock()) {
                    System.out.println("Not enough stock");
                    return;
                }
                // after order, set the stock quantity
                oi.setQuantity(totalQuantity);
                return;
            }
        }
        // if no item exist then add a new orderitem
        OrderItem newItem = new OrderItem(menuItem, quantity);
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
}
