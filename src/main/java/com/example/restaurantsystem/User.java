package com.example.restaurantsystem;

public class User {
    int id;
    String username;
    String password;
    String role;

    int getId() {
        return id;
    }
    String getUsername() {
        return username;
    }
    // maybe not do this
    String getPassword() {
        return password;
    }
    String getRole() {
        return role;
    }
    User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
    void setRole(String role) {
        this.role = role;
    }
    boolean isAdmin() {
        return role.equals("admin");
    }
}
