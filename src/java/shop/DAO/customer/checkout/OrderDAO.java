/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.customer.checkout;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import shop.context.DBcontext;
import shop.model.Order;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class OrderDAO extends DBcontext {

    public int InsertOrder(Order order) throws SQLException {
        String sql = "INSERT INTO [Order] (cus_id, total_price, tracking, order_date, payment_method) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, order.getCus_id());
            statement.setBigDecimal(2, order.getTotal_price());
            statement.setString(3, order.getTracking());
            statement.setTimestamp(4, new Timestamp(order.getOrder_date().getTime()));
            statement.setString(5, order.getPayment_method());

            statement.executeUpdate();

            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            OrderDAO orderDAO = new OrderDAO();

            // Tạo một đối tượng Order mới
            Order newOrder = new Order(
                    0, // order_id (auto-increment, không cần set)
                    1, // cus_id (giả sử khách hàng có ID = 1)
                    new BigDecimal("500000"), // total_price (500,000 VND)
                    "processing", // tracking (trạng thái đơn hàng)
                    new Date(), // order_date (ngày hiện tại)
                    "cash" // payment_method (Thanh toán khi nhận hàng)
            );

            // Gọi hàm InsertOrder để lưu vào DB
            int orderId = orderDAO.InsertOrder(newOrder);
            System.out.println("Order inserted successfully! Order ID: " + orderId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
