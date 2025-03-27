/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.customer.orders;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import shop.context.DBcontext;
import shop.model.OrderDetail;
import shop.model.Product;

/**
 *
 * @author Admin
 */
public class MyOrderDetailDAO extends DBcontext {

    public List<OrderDetail> getListOrderDetail(int orderID) {
        List<OrderDetail> listOrderDetail = new ArrayList<>();
        connection = getConnection();

        try {
            String sql = """
                         SELECT [order_detail_id]
                               ,[order_id]
                               ,[pro_id]
                               ,[size]
                               ,[quantity]
                               ,[price_per_unit]
                           FROM [ClothingShopDB].[dbo].[OrderDetail]
                           WHERE [order_id] = ?""";

            statement = connection.prepareCall(sql);
            statement.setInt(1, orderID);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int orderDetailID = resultSet.getInt(1);
                int order_ID = resultSet.getInt(2);
                int pro_id = resultSet.getInt(3);
                String size = resultSet.getString(4);
                int quantity = resultSet.getInt(5);
                BigDecimal price = resultSet.getBigDecimal(6);

//                OrderDetail orDetail = new OrderDetail(orderDetailID, order_ID, pro_id, quantity,'M', price);
                OrderDetail orDetail = new OrderDetail(orderDetailID, order_ID, pro_id, quantity, size, price);
                listOrderDetail.add(orDetail);
            }

        } catch (SQLException e) {
        }

        return listOrderDetail;
    }

    public Product getProductByID(int id) {
        Product p = null;
        connection = getConnection();

        try {
            String sql = """
                         SELECT [pro_id]
                               ,[pro_name]
                               ,[price]
                               ,[image]
                               ,[gender]
                               ,[discount]
                           FROM [ClothingShopDB].[dbo].[Product]
                           WHERE [pro_id] = ?""";
            statement = connection.prepareCall(sql);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
//                int proID = resultSet.getInt(1);
//                String name = resultSet.getString(2);
//                BigDecimal price = resultSet.getBigDecimal(3);
//                String image = resultSet.getString(4);
//                String gender = resultSet.getString(5);
//                int dis = resultSet.getInt(6);

                p = new Product();
                p.setPro_id(resultSet.getInt(1));
                p.setPro_name(resultSet.getString(2));
                p.setPrice(resultSet.getBigDecimal(3));
                p.setImage(resultSet.getString(4));
                p.setGender(resultSet.getString(5));
                p.setDiscount(resultSet.getInt(6));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return p;
    }

    public static void main(String[] args) {
        MyOrderDetailDAO dao = new MyOrderDetailDAO();

        System.out.println(dao.getListOrderDetail(1));
    }

}
