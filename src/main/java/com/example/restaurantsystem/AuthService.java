package com.example.restaurantsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class AuthService {
    private HashMap<String, User> users = new HashMap<>();

    User login(String username, String password) {
        // try catch so code doesnt throw error immediately on wrong input
        try {
            // connect to database
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );
            // sql string for sql query
            String sql = "SELECT * FROM users WHERE username = ?";
            // prepared statement to prevent sql injection attacks
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            // checks if username exists if not skip this whole block
            if (rs.next()) {

                String dbPassword = rs.getString("password");
                // compares password and db pass
                if (dbPassword.equals(password)) {
                    // create new user object then returns the object
                    User u = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role")
                    );

                    return u;
                }
            }
        // if not then return nothing or throw exception if anything fails
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    boolean register(String username, String password, String role) {
        try {
            // connect to db
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            // check for user existence
            String checkSql = "SELECT id FROM users WHERE username = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setString(1, username);

            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                return false;
            }

            // insert new user
            String insertSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement insertPs = conn.prepareStatement(insertSql);

            insertPs.setString(1, username);
            insertPs.setString(2, password);
            insertPs.setString(3, role);

            insertPs.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
