package com.example.restaurantsystem;

public class MenuItem {
    int id;
    String name;
    double price;
    String description;
    int stock;
    boolean active;
    // getter and setters
    int getId() {
        return id;
    }
    String getName() {
        return name;
    }
    double getPrice() {
        return price;
    }
    String getDescription() {
        return description;
    }
    int getStock() {
        return stock;
    }
    void setId(int id) {
        this.id = id;
    }
    void setPrice(double price) {
        this.price = price;
    }
    void setDescription(String description) {
        this.description = description;
    }
    void setStock(int stock) {
        this.stock = stock;
    }
    void setActive(boolean active) {
        this.active = active;
    }
    // check for availability includes stock availability
    boolean isAvailable() {
        return active && stock>0;
    }
}
