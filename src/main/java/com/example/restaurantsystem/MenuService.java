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

    List<MenuItem> getActiveItems() {

    }

    List<MenuItem> getByCategory(String category) {

    }

    boolean addItem(MenuItem item) {

    }

    boolean updateItem(MenuItem item) {

    }

    boolean deleteItem(int id) {

    }
}
