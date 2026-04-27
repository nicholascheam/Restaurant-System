package com.example.restaurantsystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderEdgeCaseTest {

    @Test
    void testRemoveTooMuch() {

        User user = new User(1, "test", "123", "user");

        MenuItem item = new MenuItem();
        item.setId(1);
        item.setPrice(10);
        item.setStock(10);
        item.setActive(true);

        Order order = new Order(user);

        order.addItem(item, 2);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> {
                    order.removeItem(item, 5);
                });

        assertEquals("Cannot remove more than existing quantity.", ex.getMessage());
        assertEquals(2, order.getItems().get(0).getQuantity());
    }

    @Test
    void testEmptyCartTotal() {

        User user =
                new User(1,"test","123","user");

        Order order = new Order(user);

        assertEquals(0, order.calculateTotal());
    }

    @Test
    void testDuplicateItemAddsQuantity() {

        User user =
                new User(1,"test","123","user");

        MenuItem item = new MenuItem();
        item.setId(1);
        item.setPrice(10);
        item.setStock(50);
        item.setActive(true);

        Order order = new Order(user);

        order.addItem(item,2);
        order.addItem(item,3);

        assertEquals(5, order.getItems().get(0).getQuantity());
    }
}