package com.example.restaurantsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
                    "SELECT IFNULL(SUM(total),0) " + "FROM orders WHERE DATE(order_datetime)=CURDATE()";

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
                    "SELECT COUNT(*) FROM orders WHERE DATE(order_datetime)=CURDATE()";

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
                    "JOIN menu_items m ON oi.menu_item_id = m.id " + "GROUP BY m.id, m.name " +
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
    // show stock data
    public List<String[]> getCategoryStockData() {

        List<String[]> data = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            String sql =
                    "SELECT category, SUM(stock) total_stock " +
                    "FROM menu_items " + "WHERE active = true " +
                    "GROUP BY category";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                data.add(new String[]{rs.getString("category"), rs.getString("total_stock")});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    // get orders last 7 days (NUMBER)
    public List<String[]> getOrdersLast7Days() {

        List<String[]> data = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            String sql =
                    "SELECT DATE(order_datetime) day, COUNT(*) total_orders " +
                    "FROM orders " + "WHERE order_datetime >= CURDATE() - INTERVAL 6 DAY " +
                    "GROUP BY DATE(order_datetime) " + "ORDER BY day";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                data.add(new String[]{rs.getString("day"), rs.getString("total_orders")});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
    //
    // get category sales in pie chart
    public ObservableList<PieChart.Data> getCategorySales() {

        ObservableList<PieChart.Data> list = FXCollections.observableArrayList();

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            String sql =
                    "SELECT m.category, SUM(oi.quantity) qty " +
                    "FROM order_items oi " + "JOIN menu_items m ON oi.menu_item_id = m.id " +
                    "GROUP BY m.category";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new PieChart.Data(rs.getString("category"), rs.getDouble("qty")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    // get last 7 days orders (CHART)
    public ObservableList<XYChart.Data<String, Number>> getLast7DaysOrders() {

        ObservableList<XYChart.Data<String, Number>> list = FXCollections.observableArrayList();

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            String sql =
                    "SELECT DATE(order_datetime) day, COUNT(*) total " + "FROM orders " +
                    "WHERE order_datetime >= CURDATE() - INTERVAL 6 DAY " + "GROUP BY DATE(order_datetime) " +
                    "ORDER BY day";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new XYChart.Data<>(rs.getString("day"), rs.getInt("total")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
