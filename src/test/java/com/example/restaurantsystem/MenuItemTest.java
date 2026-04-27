package com.example.restaurantsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MenuItemTest {

    @Test
    void testMenuItemCreation() {

        MenuItem item = new MenuItem();

        item.setId(1);
        item.setName("Burger");
        item.setPrice(12.5);
        item.setStock(20);
        item.setCategory("Food");
        item.setDescription("Beef burger");
        item.setActive(true);

        assertEquals(1, item.getId());
        assertEquals("Burger", item.getName());
        assertEquals(12.5, item.getPrice());
        assertEquals(20, item.getStock());
        assertEquals("Food", item.getCategory());
        assertTrue(item.isActive());
    }

    @Test
    void testStockUpdate() {

        MenuItem item = new MenuItem();

        item.setStock(10);
        item.setStock(item.getStock() - 3);

        assertEquals(7, item.getStock());
    }
}