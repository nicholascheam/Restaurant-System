package com.example.restaurantsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                    total DOUBLE
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
}