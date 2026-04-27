package com.example.restaurantsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemOptionTest {

    @Test
    void testConstructorAndGetters() {
        ItemOption option = new ItemOption(1, 100, "Size", "S,M,L", "dropdown", true);
        assertEquals(1, option.getId());
        assertEquals(100, option.getMenuItemId());
        assertEquals("Size", option.getOptionName());
        assertEquals("S,M,L", option.getOptionValues());
        assertEquals("dropdown", option.getControlType());
        assertTrue(option.isRequired());
    }

    @Test
    void testOptionWithRequiredFalse() {
        ItemOption option = new ItemOption(2, 200, "Topping", "Cheese,Pepperoni", "checkbox", false);
        assertFalse(option.isRequired());
    }

    @Test
    void testMultipleOptions() {
        ItemOption size = new ItemOption(1, 10, "Size", "Small,Medium,Large", "radio", true);
        ItemOption extra = new ItemOption(2, 10, "Extra", "Cheese", "checkbox", false);

        assertEquals(1, size.getId());
        assertEquals("Size", size.getOptionName());
        assertTrue(size.isRequired());

        assertEquals(2, extra.getId());
        assertFalse(extra.isRequired());
    }
}