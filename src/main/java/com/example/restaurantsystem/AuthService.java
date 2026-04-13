package com.example.restaurantsystem;

import java.util.HashMap;

public class AuthService {
    private HashMap<String, User> users = new HashMap<>();

    User login(String username, String password) {
        User u = users.get(username);
        if (u == null) {
            return null;
        }
        if (u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }
    void register(User user) {
        if (users.containsKey(user.getUsername())) {
            System.out.println("Username already exists");
            return;
        }

        users.put(user.getUsername(), user);
    }
}
