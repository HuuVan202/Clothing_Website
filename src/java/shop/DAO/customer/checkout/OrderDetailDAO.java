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
import shop.model.OrderDetail;

/**
 *
 * @author Admin
 */
public class OrderDetailDAO extends DBcontext {

    /**
     * Inserts an order detail into the database
     *
     * @param orderDetail The OrderDetail object to be inserted
     * @throws SQLException If a database access error occurs
     */
    public void insertOrderDetail(OrderDetail orderDetail) throws SQLException {
        String sql = "INSERT INTO OrderDetail (order_id, pro_id, quantity, price_per_unit) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderDetail.getOrder_id());
            statement.setInt(2, orderDetail.getPro_id());
            statement.setInt(3, orderDetail.getQuantity());
            statement.setBigDecimal(4, orderDetail.getPrice_per_unit());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted == 0) {
                throw new SQLException("Inserting order detail failed, no rows affected.");
            }
        }
    }

    public static void main(String[] args) {
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

        OrderDetail orderDetail = new OrderDetail(0, 1, 101, 2, new BigDecimal("299000"));

        try {
            orderDetailDAO.insertOrderDetail(orderDetail);
            System.out.println("Order detail inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
