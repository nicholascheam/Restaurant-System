package com.example.restaurantsystem;

public class MenuItem {
    int id;
    String name;
    double price;
    String description;
    int stock;
    boolean active;
    String category;
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
    String getCategory() {
        return category;
    }
    void setId(int id) {
        this.id = id;
    }
    void setName(String name) {
        this.name = name;
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
    void setCategory(String category) {
        this.category = category;
    }
    // check for availability includes stock availability
    boolean isAvailable() {
        return active && stock>0;
    }
}
