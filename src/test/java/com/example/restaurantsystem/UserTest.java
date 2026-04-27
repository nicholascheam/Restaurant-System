package com.example.restaurantsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testUserCreation() {

        User user = new User(1, "nicholas", "123", "user");

        assertEquals(1, user.getId());
        assertEquals("nicholas", user.getUsername());
        assertEquals("123", user.getPassword());
        assertEquals("user", user.getRole());
    }

    @Test
    void testAdminRole() {

        User admin = new User(2, "admin", "123", "admin");

        assertEquals("admin", admin.getRole());
    }
}