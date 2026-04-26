package com.example.restaurantsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DashboardService {
    // getting information from db
    public double getRevenueToday() {
        return getDoubleValue("SELECT IFNULL(SUM(total),0) FROM orders WHERE DATE(order_datetime)=CURDATE()");
    }
    public double getRevenueLast7Days() {
        return getDoubleValue("SELECT IFNULL(SUM(total),0) FROM orders WHERE order_datetime >= NOW() - INTERVAL 7 DAY");
    }
    public int getOrdersToday() {
        return getIntValue("SELECT COUNT(*) FROM orders WHERE DATE(order_datetime)=CURDATE()");
    }
    public int getOrdersLast7Days() {
        return getIntValue("SELECT COUNT(*) FROM orders WHERE order_datetime >= NOW() - INTERVAL 7 DAY");
    }
    public double getAverageOrderValue() {
        return getDoubleValue("SELECT IFNULL(AVG(total),0) FROM orders");
    }
    // top selling items
    public List<String> getTopSellingItems() {

        List<String> list = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();

            String sql =
                    "SELECT m.name, SUM(oi.quantity) sold " + "FROM order_items oi " +
                    "JOIN menu_items m ON oi.menu_item_id=m.id " + "GROUP BY m.name " + "ORDER BY sold DESC LIMIT 5";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            int rank = 1;

            while (rs.next()) {
                list.add(rank++ + ". " + rs.getString("name") + " (" + rs.getInt("sold") + " sold)");
            }
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    // low stock items
    public List<String> getLowStockItems() {

        List<String> list = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement("SELECT name, stock FROM menu_items WHERE stock <= 5");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("name") + " (" + rs.getInt("stock") + " left)");
            }
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    // last 7 days performance
    public List<String> getLast7DayPerformance() {

        List<String> list = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();

            String sql =
                    "SELECT DATE(order_datetime) d, " + "COUNT(*) orders_count, " + "SUM(total) revenue " +
                    "FROM orders " + "WHERE order_datetime >= NOW() - INTERVAL 7 DAY " + "GROUP BY DATE(order_datetime)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getDate("d") + " | Orders: " + rs.getInt("orders_count") + " | RM " + rs.getDouble("revenue"));
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    // simple summaries
    public List<String> getBusinessInsights() {

        List<String> list = new ArrayList<>();

        if (getRevenueLast7Days() > 1000) {
            list.add("- Strong weekly sales performance.");
        }

        if (!getLowStockItems().isEmpty()) {
            list.add("- Some items require urgent restocking.");
        }

        return list;
    }
    public ObservableList<XYChart.Data<String, Number>> getLast7DaysRevenue() {

        ObservableList<XYChart.Data<String, Number>> list = FXCollections.observableArrayList();

        String sql = """
                    SELECT DATE(order_datetime) day, SUM(total) revenue
                    FROM orders
                    WHERE order_datetime >= CURDATE() - INTERVAL 6 DAY
                    GROUP BY DATE(order_datetime)
                    ORDER BY day
                    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new XYChart.Data<>(rs.getString("day"), rs.getDouble("revenue")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    // for sql nums with decimals
    private double getDoubleValue(String sql) {

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double value = rs.getDouble(1);
                conn.close();
                return value;
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    // sql int values
    private int getIntValue(String sql) {

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int value = rs.getInt(1);
                conn.close();
                return value;
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
