package com.example.restaurantsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuService {
    // get all the individual food items from db
    public List<MenuItem> getAllItems() {

        List<MenuItem> list = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();

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
                item.setActive(rs.getBoolean("active"));

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
            Connection conn = DBConnection.getConnection();

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
    // get the category
    public List<String> getCategories() {

        List<String> categories = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();

            String sql =
                    "SELECT DISTINCT category " + "FROM menu_items " +
                    "WHERE active = true " + "AND stock > 0 " + "ORDER BY category";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                categories.add(rs.getString("category")
                );
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }
    // get the items by category
    public List<MenuItem> getByCategory(String category) {

        List<MenuItem> list = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();

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
    // count the number of things in category
    public Map<String, Integer> getCategoryCounts() {

        Map<String, Integer> map = new HashMap<>();

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT category, active, stock FROM menu_items";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String category = rs.getString("category");
                boolean active = rs.getBoolean("active");
                int stock = rs.getInt("stock");

                if (!map.containsKey(category)) {
                    map.put(category, 0);
                }

                if (active && stock > 0) {
                    map.put(category, map.get(category) + 1);
                }
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
    // add items to db
    public boolean addItem(MenuItem item) {

        try {
            Connection conn = DBConnection.getConnection();

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
            Connection conn = DBConnection.getConnection();

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
            Connection conn = DBConnection.getConnection();

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
    // hard delete method
    public boolean hardDeleteItem(int id) {

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "DELETE FROM menu_items WHERE id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    // make item active method
    public boolean activateItem(int id) {
        String sql = "UPDATE menu_items SET active = 1 WHERE id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
