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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // create table
    private static void createTables() {

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            Statement stmt = conn.createStatement();

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50) UNIQUE,
                    password VARCHAR(50),
                    role VARCHAR(20)
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS menu_items (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(100),
                    price DOUBLE,
                    description TEXT,
                    stock INT,
                    category VARCHAR(50),
                    active BOOLEAN
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS orders (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    user_id INT,
                    total DOUBLE,
                    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS order_items (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    order_id INT,
                    menu_item_id INT,
                    quantity INT,
                    price DOUBLE
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
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

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
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

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
}