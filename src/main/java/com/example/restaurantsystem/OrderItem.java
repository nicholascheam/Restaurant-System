package com.example.restaurantsystem;

public class OrderItem {
    MenuItem menuItem;
    int quantity;
    double priceAtPurchase;
    private String customText;
    OrderItem(MenuItem menuItem, int quantity) {
        this(menuItem, quantity, "");
    }

    // constructor with customization
    OrderItem(MenuItem menuItem, int quantity, String customText) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.priceAtPurchase = menuItem.getPrice();
        this.customText = customText;
    }
    // getter and setters
    MenuItem getMenuItem() {
        return menuItem;
    }

    int getQuantity() {
        return quantity;
    }
    double getPriceAtPurchase() {
        return priceAtPurchase;
    }
    int getMenuItemId() {
        return menuItem.getId();
    }
    String getCustomText() {
        return customText;
    }
    void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // subtotaling the item ONLY, goes into order class for total
    double getSubtotal() {
        return quantity * priceAtPurchase;
    }
}
