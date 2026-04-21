package com.example.restaurantsystem;

import java.sql.*;

public class OrderService {
    Connection conn = null;
    void saveOrder(Order order) {
        try {
            // connect to db
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );
            // make sure to not immediately do sql command before finishing order
            conn.setAutoCommit(false);

            // insert order
            String orderSql = "INSERT INTO orders (user_id, order_datetime) VALUES (?, ?)";
            PreparedStatement orderPs = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);

            orderPs.setInt(1, order.getUser().getId());
            orderPs.setObject(2, order.getDateTime());

            orderPs.executeUpdate();

            // get unique session order id separated from orderid
            ResultSet rs = orderPs.getGeneratedKeys();

            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }
            if (orderId == 0) {
                throw new RuntimeException("Failed to create order");
            }
            // insert order items
            String itemSql = "INSERT INTO order_items (order_id, menu_item_id, quantity, price_at_purchase) VALUES (?, ?, ?, ?)";
            PreparedStatement itemPs = conn.prepareStatement(itemSql);

            for (OrderItem oi : order.getItems()) {
                itemPs.setInt(1, orderId);
                itemPs.setInt(2, oi.getMenuItemId());
                itemPs.setInt(3, oi.getQuantity());
                itemPs.setDouble(4, oi.getPriceAtPurchase()); // priceAtPurchase

                itemPs.executeUpdate();
            }

            // finally do sql queries
            conn.commit();
        // if anything breaks throw exception, but try to rollback before making final exception again
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    // place order
    public boolean placeOrder(Order order) {

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/restaurant_db",
                    "root",
                    ""
            );

            conn.setAutoCommit(false);

            // check stock
            for (OrderItem oi : order.getItems()) {

                String checkSql = "SELECT stock FROM menu_items WHERE id = ?";

                PreparedStatement checkPs = conn.prepareStatement(checkSql);

                checkPs.setInt(1, oi.getMenuItem().getId());

                ResultSet rs = checkPs.executeQuery();

                if (rs.next()) {

                    int stock = rs.getInt("stock");

                    if (stock < oi.getQuantity()) {
                        conn.rollback();
                        conn.close();
                        return false;
                    }
                }
            }

            // insert order
            String orderSql =
                    "INSERT INTO orders(user_id, total) VALUES (?, ?)";

            PreparedStatement orderPs = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);

            orderPs.setInt(1, order.getUser().getId());

            orderPs.setDouble(2, order.calculateTotal());

            orderPs.executeUpdate();

            ResultSet keys = orderPs.getGeneratedKeys();

            keys.next();

            int orderId = keys.getInt(1);

            // insert order items then deduct stock
            for (OrderItem oi : order.getItems()) {

                String itemSql =
                        "INSERT INTO order_items " + "(order_id, menu_item_id, quantity, price_at_purchase, subtotal) " +
                        "VALUES (?, ?, ?, ?, ?)";

                PreparedStatement itemPs = conn.prepareStatement(itemSql);

                itemPs.setInt(1, orderId);
                itemPs.setInt(2, oi.getMenuItem().getId());
                itemPs.setInt(3, oi.getQuantity());
                itemPs.setDouble(4, oi.getPriceAtPurchase());
                itemPs.setDouble(5, oi.getPriceAtPurchase() * oi.getQuantity());

                itemPs.executeUpdate();

                String deductSql =
                        "UPDATE menu_items " + "SET stock = stock - ? " + "WHERE id = ?";

                PreparedStatement deductPs = conn.prepareStatement(deductSql);

                deductPs.setInt(1, oi.getQuantity());

                deductPs.setInt(2, oi.getMenuItem().getId());

                deductPs.executeUpdate();
            }

            conn.commit();
            conn.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}