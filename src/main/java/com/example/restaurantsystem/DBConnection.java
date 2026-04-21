package com.example.restaurantsystem;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/restaurant_db",
                "root",
                ""
        );
    }
}
