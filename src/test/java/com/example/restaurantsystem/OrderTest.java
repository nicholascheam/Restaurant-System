package com.example.restaurantsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    @Test
    void testAddItem() {

        User user = new User(1,"test","123","user");

        MenuItem burger = new MenuItem();
        burger.setId(1);
        burger.setName("Burger");
        burger.setPrice(10);
        burger.setStock(50);
        burger.setActive(true);

        Order order = new Order(user);

        order.addItem(burger,2);

        assertEquals(1, order.getItems().size());
        assertEquals(2, order.getItems().get(0).getQuantity());
    }

    @Test
    void testRemoveItem() {

        User user = new User(1,"test","123","user");

        MenuItem burger = new MenuItem();
        burger.setId(1);
        burger.setPrice(10);
        burger.setStock(50);
        burger.setActive(true);

        Order order = new Order(user);

        order.addItem(burger,5);
        order.removeItem(burger,2);

        assertEquals(3, order.getItems().get(0).getQuantity());
    }

    @Test
    void testCalculateTotal() {

        User user = new User(1,"test","123","user");

        MenuItem burger = new MenuItem();
        burger.setId(1);
        burger.setPrice(10);
        burger.setStock(50);
        burger.setActive(true);

        MenuItem fries = new MenuItem();
        fries.setId(2);
        fries.setPrice(5);
        fries.setStock(50);
        fries.setActive(true);

        Order order = new Order(user);

        order.addItem(burger,2);
        order.addItem(fries,3);

        assertEquals(35, order.calculateTotal());
    }

    @Test
    void testClearCart() {

        User user = new User(1,"test","123","user");

        MenuItem burger = new MenuItem();
        burger.setId(1);
        burger.setPrice(10);
        burger.setStock(50);
        burger.setActive(true);

        Order order = new Order(user);

        order.addItem(burger,2);
        order.clear();

        assertTrue(order.getItems().isEmpty());
    }

    @Test
    void testInvalidQuantity() {

        User user = new User(1,"test","123","user");

        MenuItem burger = new MenuItem();
        burger.setId(1);
        burger.setPrice(10);
        burger.setStock(50);
        burger.setActive(true);

        Order order = new Order(user);

        assertThrows(IllegalArgumentException.class, () -> {
            order.addItem(burger,0);
        });
    }
}