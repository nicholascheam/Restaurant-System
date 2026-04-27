package com.example.restaurantsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderItemTest {

    @Test
    void testSubtotal() {

        MenuItem burger = new MenuItem();
        burger.setId(1);
        burger.setPrice(15);

        OrderItem oi = new OrderItem(burger, 3);

        assertEquals(45, oi.getSubtotal());
    }
    @Test
    void testConstructorWithCustomText() {
        MenuItem pizza = new MenuItem();
        pizza.setPrice(12.99);
        OrderItem oi = new OrderItem(pizza, 2, "Extra cheese");
        assertEquals(pizza, oi.getMenuItem());
        assertEquals(2, oi.getQuantity());
        assertEquals("Extra cheese", oi.getCustomText());
        assertEquals(25.98, oi.getSubtotal(), 0.01);
    }

    @Test
    void testConstructorWithoutCustomTextUsesEmptyString() {
        MenuItem pasta = new MenuItem();
        pasta.setPrice(10.50);
        OrderItem oi = new OrderItem(pasta, 1);
        assertEquals("", oi.getCustomText());
        assertEquals(10.50, oi.getSubtotal(), 0.01);
    }

    @Test
    void testZeroQuantitySubtotal() {
        MenuItem drink = new MenuItem();
        drink.setPrice(3.0);
        OrderItem oi = new OrderItem(drink, 0);
        assertEquals(0.0, oi.getSubtotal(), 0.01);
    }

    @Test
    void testPriceAtPurchaseIsCapturedAtConstruction() {
        MenuItem burger = new MenuItem();
        burger.setPrice(15);
        OrderItem oi = new OrderItem(burger, 2);
        assertEquals(15, oi.getPriceAtPurchase(), 0.01);
        burger.setPrice(20);
        assertEquals(30, oi.getSubtotal());
    }

    @Test
    void testUpdateQuantity() {
        MenuItem fries = new MenuItem();
        fries.setPrice(4);
        OrderItem oi = new OrderItem(fries, 1);
        oi.setQuantity(3);
        assertEquals(3, oi.getQuantity());
        assertEquals(12, oi.getSubtotal());
    }

    @Test
    void testGetMenuItemId() {
        MenuItem item = new MenuItem();
        item.setId(42);
        OrderItem oi = new OrderItem(item, 1);
        assertEquals(42, oi.getMenuItemId());
    }
}