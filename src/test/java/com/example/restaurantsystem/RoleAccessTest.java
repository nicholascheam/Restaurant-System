package com.example.restaurantsystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleAccessTest {

    @Test
    void testAdminRole() {

        User admin = new User(1,"admin","123","admin");

        assertEquals("admin", admin.getRole());
    }

    @Test
    void testNormalUserRole() {

        User user = new User(2,"nick","123","user");

        assertEquals("user", user.getRole());
    }

    @Test
    void testRolesDifferent() {

        User admin = new User(1,"admin","123","admin");

        User user = new User(2,"nick","123","user");

        assertNotEquals(admin.getRole(), user.getRole());
    }
}