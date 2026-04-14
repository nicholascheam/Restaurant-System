package com.example.restaurantsystem;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseTest {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            System.out.println("Connected to database!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}