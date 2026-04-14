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
            String orderSql = "INSERT INTO orders (user_id, datetime) VALUES (?, ?)";
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
}