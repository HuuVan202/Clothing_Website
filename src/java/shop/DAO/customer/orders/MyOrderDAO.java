/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.customer.orders;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import shop.context.DBcontext;
import shop.model.Order;

/**
 *
 * @author Admin
 */
public class MyOrderDAO extends DBcontext {

    public List<Order> getListOrderByID(int cusId) {
        List<Order> listOrder = new ArrayList<>();
        connection = getConnection();

        try {
            String sql = """
                         SELECT [order_id]
                               ,[cus_id]
                               ,[total_price]
                               ,[tracking]
                               ,[order_date]
                               ,[payment_method]
                           FROM [ClothingShopDB].[dbo].[Order]
                           WHERE [cus_id] = ?""";

            statement = connection.prepareCall(sql);
            statement.setInt(1, cusId);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int order_id = resultSet.getInt(1);
                int cus_id = resultSet.getInt(2);
                BigDecimal total_price = resultSet.getBigDecimal(3);
                String tracking = resultSet.getString(4);
                Date order_date = resultSet.getDate(5);
                String payment = resultSet.getString(6);

                Order order = new Order(order_id, cus_id, total_price, tracking, order_date, payment);
                listOrder.add(order);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return listOrder;
    }

    public static void main(String[] args) {
        MyOrderDAO dao = new MyOrderDAO();

        System.out.println(dao.getListOrderByID(51));
    }
}
