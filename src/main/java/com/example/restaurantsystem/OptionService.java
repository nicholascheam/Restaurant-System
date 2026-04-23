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
    // adding option (sql)
    public boolean addOption(ItemOption op) {

        try {
            Connection conn = DBConnection.getConnection();

            String sql =
                    "INSERT INTO item_options " +
                            "(menu_item_id, option_name, option_values, control_type, required) " +
                            "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, op.getMenuItemId());
            ps.setString(2, op.getOptionName());
            ps.setString(3, op.getOptionValues());
            ps.setString(4, op.getControlType());
            ps.setBoolean(5, op.isRequired());

            ps.executeUpdate();
            conn.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    // updating option (sql)
    public boolean updateOption(ItemOption op) {

        try {
            Connection conn = DBConnection.getConnection();

            String sql =
                    "UPDATE item_options " +
                            "SET option_name=?, option_values=?, control_type=?, required=? " +
                            "WHERE id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, op.getOptionName());
            ps.setString(2, op.getOptionValues());
            ps.setString(3, op.getControlType());
            ps.setBoolean(4, op.isRequired());
            ps.setInt(5, op.getId());

            ps.executeUpdate();
            conn.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    // delete option (sql)
    public boolean deleteOption(int id) {

        try {
            Connection conn = DBConnection.getConnection();

            String sql =
                    "DELETE FROM item_options WHERE id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();
            conn.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}