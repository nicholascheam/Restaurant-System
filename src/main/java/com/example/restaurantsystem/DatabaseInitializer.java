package com.example.restaurantsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {
    // connect to mysql then create table
    public static void initialize() {

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/",
                    "root",
                    ""
            );

            Statement stmt = conn.createStatement();

            stmt.executeUpdate(
                    "CREATE DATABASE IF NOT EXISTS restaurant_db"
            );

            stmt.close();
            conn.close();

            createTables();
            seedAdmin();
            seedMenuItems();
            seedItemOptions();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // create table
    private static void createTables() {

        try {
            Connection conn = DBConnection.getConnection();

            Statement stmt = conn.createStatement();

            stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS users (
                id INT PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(50) UNIQUE NOT NULL,
                password VARCHAR(50) NOT NULL,
                role VARCHAR(20) NOT NULL,
                created_at DATETIME
                DEFAULT CURRENT_TIMESTAMP
            )
        """);

            stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS menu_items (
                id INT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(100) NOT NULL,
                price DOUBLE NOT NULL,
                description TEXT,
                stock INT DEFAULT 0,
                category VARCHAR(50),
                active BOOLEAN DEFAULT TRUE,
                created_at DATETIME
                DEFAULT CURRENT_TIMESTAMP
            )
        """);

            stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS orders (
                id INT PRIMARY KEY AUTO_INCREMENT,
                user_id INT NOT NULL,
                total DOUBLE DEFAULT 0,
                status VARCHAR(20)
                DEFAULT 'Completed',
                order_datetime DATETIME
                DEFAULT CURRENT_TIMESTAMP
            )
        """);

            stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS order_items (
                id INT PRIMARY KEY AUTO_INCREMENT,
                order_id INT NOT NULL,
                menu_item_id INT NOT NULL,
                quantity INT NOT NULL,
                price_at_purchase DOUBLE NOT NULL,
                subtotal DOUBLE NOT NULL,
                custom_text VARCHAR(255),
                added_datetime DATETIME DEFAULT CURRENT_TIMESTAMP
            )
            """);

            stmt.executeUpdate("""
            CREATE TABLE IF NOT EXISTS item_options (
                id INT PRIMARY KEY AUTO_INCREMENT,
                menu_item_id INT NOT NULL,
                option_name VARCHAR(50),
                option_values VARCHAR(255),
                control_type VARCHAR(20),
                required BOOLEAN DEFAULT FALSE
            )
        """);

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // default user 1
    private static void seedAdmin() {

        try {
            Connection conn = DBConnection.getConnection();

            Statement stmt = conn.createStatement();

            stmt.executeUpdate("""
            INSERT INTO users (username, password, role)
            SELECT 'admin', 'admin123', 'admin'
            WHERE NOT EXISTS (
                SELECT 1 FROM users WHERE username = 'admin'
            )
        """);

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // default menu item when load in
    private static void seedMenuItems() {

        try {
            Connection conn = DBConnection.getConnection();

            Statement stmt = conn.createStatement();

            stmt.executeUpdate("""
            INSERT INTO menu_items
            (name, price, description, stock, category, active)

            SELECT 'Burger', 8.50,
                   'Classic beef burger',
                   20, 'Burgers', true
            WHERE NOT EXISTS (
                SELECT 1 FROM menu_items WHERE name='Burger'
            )
        """);

            stmt.executeUpdate("""
            INSERT INTO menu_items
            (name, price, description, stock, category, active)

            SELECT 'Fries', 4.00,
                   'Crispy french fries',
                   30, 'Sides', true
            WHERE NOT EXISTS (
                SELECT 1 FROM menu_items WHERE name='Fries'
            )
        """);

            stmt.executeUpdate("""
            INSERT INTO menu_items
            (name, price, description, stock, category, active)

            SELECT 'Coke', 3.00,
                   'Cold soft drink',
                   25, 'Drinks', true
            WHERE NOT EXISTS (
                SELECT 1 FROM menu_items WHERE name='Coke'
            )
        """);

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // default food item options
    private static void seedItemOptions() {

        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();

            // Burger options
            stmt.executeUpdate("""
            INSERT INTO item_options
            (menu_item_id, option_name, option_values, control_type, required)

            SELECT id, 'Size', 'Regular,Large', 'radio', true
            FROM menu_items
            WHERE name='Burger'
            AND NOT EXISTS (
                SELECT 1 FROM item_options
                WHERE menu_item_id = menu_items.id
                AND option_name='Size'
            )
        """);

            stmt.executeUpdate("""
            INSERT INTO item_options
            (menu_item_id, option_name, option_values, control_type, required)

            SELECT id, 'Cheese', 'Yes,No', 'radio', false
            FROM menu_items
            WHERE name='Burger'
            AND NOT EXISTS (
                SELECT 1 FROM item_options
                WHERE menu_item_id = menu_items.id
                AND option_name='Cheese'
            )
        """);

            // Coke options
            stmt.executeUpdate("""
            INSERT INTO item_options
            (menu_item_id, option_name, option_values, control_type, required)

            SELECT id, 'Ice', 'Normal,Less,No Ice', 'combo', false
            FROM menu_items
            WHERE name='Coke'
            AND NOT EXISTS (
                SELECT 1 FROM item_options
                WHERE menu_item_id = menu_items.id
                AND option_name='Ice'
            )
        """);

            stmt.executeUpdate("""
            INSERT INTO item_options
            (menu_item_id, option_name, option_values, control_type, required)

            SELECT id, 'Sugar', 'Normal,Less,None', 'combo', false
            FROM menu_items
            WHERE name='Coke'
            AND NOT EXISTS (
                SELECT 1 FROM item_options
                WHERE menu_item_id = menu_items.id
                AND option_name='Sugar'
            )
        """);

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}