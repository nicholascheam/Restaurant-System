package com.example.restaurantsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OptionService {
    // get options for each item
    public List<ItemOption> getOptions(int menuItemId) {

        List<ItemOption> list = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM item_options WHERE menu_item_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, menuItemId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                list.add(new ItemOption(rs.getInt("id"), rs.getInt("menu_item_id"),
                        rs.getString("option_name"), rs.getString("option_values"),
                        rs.getString("control_type"), rs.getBoolean("required")));
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}