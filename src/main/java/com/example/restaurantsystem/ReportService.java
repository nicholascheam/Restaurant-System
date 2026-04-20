package com.example.restaurantsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportService {
    // retrieve the revenue from today
    public double getRevenueToday() {

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            String sql =
                    "SELECT IFNULL(SUM(total),0) " + "FROM orders WHERE DATE(order_time)=CURDATE()";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getDouble(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    // retrieve the orders from today
    public int getOrdersToday() {

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            String sql =
                    "SELECT COUNT(*) FROM orders WHERE DATE(order_time)=CURDATE()";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    // retrieve best selling item
    public String getBestSeller() {

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            String sql =
                    "SELECT m.name AS menu_name, SUM(oi.quantity) qty " + "FROM order_items oi " +
                    "JOIN menu_items m ON oi.menu_item_id = m.id " + "GROUP BY m.name " +
                    "ORDER BY qty DESC LIMIT 1";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getString("menu_name");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "None";
    }
    // show low stock items
    public int getLowStockCount() {

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            String sql =
                    "SELECT COUNT(*) FROM menu_items WHERE stock <= 3 AND active = true";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

}
