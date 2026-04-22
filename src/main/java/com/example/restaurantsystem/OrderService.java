package com.example.restaurantsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class OrderService {

    private Connection conn;
    // place order
    public boolean placeOrder(Order order) {

        if (!isValidOrder(order)) return false;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            if (!checkStock(order)) {
                rollback();
                return false;
            }

            int orderId = insertOrder(order);

            if (orderId == 0) {
                rollback();
                return false;
            }

            insertOrderItems(orderId, order);
            deductStocks(order);
            conn.commit();
            conn.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            rollback();
            return false;
        }
    }

    // validate order
    private boolean isValidOrder(Order order) {

        if (order == null) return false;
        if (order.getUser() == null) return false;
        if (order.getItems() == null) return false;
        if (order.getItems().isEmpty()) return false;

        for (OrderItem oi : order.getItems()) {
            if (oi.getQuantity() <= 0) return false;
        }

        return true;
    }

    // check stock first
    private boolean checkStock(Order order) {

        try {
            for (OrderItem oi : order.getItems()) {

                String sql =
                        "SELECT stock FROM menu_items WHERE id = ?";

                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, oi.getMenuItem().getId());

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    int stock = rs.getInt("stock");

                    if (stock < oi.getQuantity()) {
                        return false;
                    }

                } else {
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    // insert parent order row
    private int insertOrder(Order order) {

        try {
            String sql =
                    "INSERT INTO orders (user_id, total) VALUES (?, ?)";

            PreparedStatement ps =
                    conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, order.getUser().getId());
            ps.setDouble(2, order.calculateTotal());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // insert child items
    private void insertOrderItems(int orderId, Order order) {

        try {
            String sql =
                    "INSERT INTO order_items " + "(order_id, menu_item_id, quantity, price_at_purchase, subtotal, custom_text) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            for (OrderItem oi : order.getItems()) {

                ps.setInt(1, orderId);
                ps.setInt(2, oi.getMenuItem().getId());
                ps.setInt(3, oi.getQuantity());
                ps.setDouble(4, oi.getPriceAtPurchase());
                ps.setDouble(5, oi.getPriceAtPurchase() * oi.getQuantity());
                ps.setString(6, oi.getCustomText());

                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // deduct stock after success
    private void deductStocks(Order order) {

        try {
            String sql =
                    "UPDATE menu_items SET stock = stock - ? WHERE id = ?";

            PreparedStatement ps =
                    conn.prepareStatement(sql);

            for (OrderItem oi : order.getItems()) {

                ps.setInt(1, oi.getQuantity());
                ps.setInt(2, oi.getMenuItem().getId());

                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // rollback transaction
    private void rollback() {

        try {
            if (conn != null) {
                conn.rollback();
                conn.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}