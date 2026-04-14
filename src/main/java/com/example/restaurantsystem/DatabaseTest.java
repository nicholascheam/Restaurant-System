package com.example.restaurantsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
// class for testing connection with sql server, will get deleted maybe
public class DatabaseTest {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "nick");
            ps.setString(2, "123");
            ps.setString(3, "admin");

            ps.executeUpdate();

            System.out.println("User inserted!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}