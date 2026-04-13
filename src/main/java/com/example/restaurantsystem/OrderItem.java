package com.example.restaurantsystem;

public class OrderItem {
    MenuItem menuItem;
    int quantity;
    double priceAtPurchase;

    MenuItem getMenuItem() {
        return menuItem;
    }

    int getQuantity() {
        return quantity;
    }
    void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.priceAtPurchase = menuItem.getPrice();
    }


    double getSubtotal() {
        return quantity * priceAtPurchase;
    }
}
