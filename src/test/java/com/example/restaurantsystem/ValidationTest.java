package com.example.restaurantsystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTest {

    @Test
    void testNegativePrice() {

        MenuItem item = new MenuItem();
        item.setPrice(-5);

        assertTrue(item.getPrice() < 0);
    }

    @Test
    void testZeroStock() {

        MenuItem item = new MenuItem();
        item.setStock(0);

        assertEquals(0, item.getStock());
    }

    @Test
    void testBlankName() {

        MenuItem item = new MenuItem();
        item.setName("");

        assertTrue(item.getName().isBlank());
    }

    @Test
    void testInvalidOrderQuantity() {

        User user = new User(1,"test","123","user");

        MenuItem item = new MenuItem();
        item.setId(1);
        item.setPrice(10);
        item.setStock(10);
        item.setActive(true);

        Order order = new Order(user);

        assertThrows(IllegalArgumentException.class, () -> {
            order.addItem(item,-1);
        });
    }
}