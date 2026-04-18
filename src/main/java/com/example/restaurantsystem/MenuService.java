package com.example.restaurantsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MenuService {
    // get all the individual food items from db
    public List<MenuItem> getAllItems() {

        List<MenuItem> list = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            String sql = "SELECT * FROM menu_items";
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                MenuItem item = new MenuItem();

                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setPrice(rs.getDouble("price"));
                item.setDescription(rs.getString("description"));
                item.setStock(rs.getInt("stock"));
                item.setCategory(rs.getString("category"));

                list.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    // get active items from db
    public List<MenuItem> getActiveItems() {

        List<MenuItem> list = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            String sql = "SELECT * FROM menu_items WHERE active = true";
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MenuItem item = new MenuItem();

                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setPrice(rs.getDouble("price"));
                item.setDescription(rs.getString("description"));
                item.setStock(rs.getInt("stock"));
                item.setCategory(rs.getString("category"));
                item.setActive(rs.getBoolean("active"));

                list.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    // get the items by category
    public List<MenuItem> getByCategory(String category) {

        List<MenuItem> list = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            String sql = "SELECT * FROM menu_items WHERE category = ? AND active = true";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, category);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MenuItem item = new MenuItem();

                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setPrice(rs.getDouble("price"));
                item.setStock(rs.getInt("stock"));
                item.setCategory(rs.getString("category"));
                item.setDescription(rs.getString("description"));
                item.setActive(rs.getBoolean("active"));

                list.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    // add items to db
    public boolean addItem(MenuItem item) {

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            String sql = "INSERT INTO menu_items (name, price, description, stock, category, active) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, item.getName());
            ps.setDouble(2, item.getPrice());
            ps.setString(3, item.getDescription());
            ps.setInt(4, item.getStock());
            ps.setString(5, item.getCategory());
            ps.setBoolean(6, item.isActive());

            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    // update item but all at once
    public boolean updateItem(MenuItem item) {

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            String sql =
                    "UPDATE menu_items " +
                            "SET name=?, price=?, description=?, stock=?, category=?, active=? " +
                            "WHERE id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, item.getName());
            ps.setDouble(2, item.getPrice());
            ps.setString(3, item.getDescription());
            ps.setInt(4, item.getStock());
            ps.setString(5, item.getCategory());
            ps.setBoolean(6, item.isActive());
            ps.setInt(7, item.getId());

            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    // soft delete method
    public boolean deleteItem(int id) {

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            String sql = "UPDATE menu_items SET active = false WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
