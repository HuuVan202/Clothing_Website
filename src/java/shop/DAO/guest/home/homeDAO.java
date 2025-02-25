/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.guest.home;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import shop.context.DBcontext;
import shop.model.Product;

/**
 *
 * @author Dinh_Hau
 */
public class homeDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public List<Product> getNewArrivals() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT top 50 p.pro_id, p.pro_name, p.image, p.discount, p.price AS original_price, \n"
                + "       CASE \n"
                + "           WHEN p.discount > 0 THEN p.price * (1 - p.discount / 100.0)  \n"
                + "           ELSE p.price \n"
                + "       END AS discounted_price\n"
                + "FROM Product p\n"
                + "WHERE p.status = 'active'\n"
                + "ORDER BY p.pro_id DESC;";

        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setPro_id(rs.getInt(1)); 
                p.setPro_name(rs.getString(2));
                p.setImage(rs.getString(3));
                p.setDiscount(rs.getInt(4));
                p.setPrice(rs.getBigDecimal(5));
                p.setDiscountedPrice(rs.getBigDecimal(6));

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> getBestSellers() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.pro_id, p.pro_name, p.image, p.discount, p.price AS original_price, \n"
                + "    CASE \n"
                + "        WHEN p.discount > 0 THEN p.price * (1 - p.discount / 100.0)  \n"
                + "        ELSE p.price \n"
                + "    END AS discounted_price, \n"
                + "    SUM(od.quantity) AS total_sold\n"
                + "FROM OrderDetail od    \n"
                + "JOIN Product p ON od.pro_id = p.pro_id\n"
                + "JOIN [Order] o ON od.order_id = o.order_id\n"
                + "WHERE o.tracking = 'delivered'\n"
                + "AND p.status = 'active'\n"
                + "GROUP BY p.pro_id, p.pro_name, p.price, p.image, p.discount\n"
                + "ORDER BY total_sold DESC";

        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setPro_id(rs.getInt(1));
                p.setPro_name(rs.getString(2));
                p.setImage(rs.getString(3));
                p.setDiscount(rs.getInt(4));
                p.setPrice(rs.getBigDecimal(5));
                p.setDiscountedPrice(rs.getBigDecimal(6));

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> getDiscountedProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT TOP 50 p.pro_id, p.pro_name, p.image, p.discount, p.price AS original_price, \n"
                + " CASE \n"
                + " WHEN p.discount > 0 THEN p.price * (1 - p.discount / 100.0)  \n"
                + " ELSE p.price \n"
                + " END AS discounted_price  \n"
                + "FROM Product p\n"
                + "WHERE p.discount > 0\n"
                + "AND p.status = 'active'\n"
                + "ORDER BY p.discount DESC;";

        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setPro_id(rs.getInt(1)); // Lấy pro_id từ cột đầu tiên
                p.setPro_name(rs.getString(2));
                p.setImage(rs.getString(3));
                p.setDiscount(rs.getInt(4));
                p.setPrice(rs.getBigDecimal(5));
                p.setDiscountedPrice(rs.getBigDecimal(6));

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> getRecommendProducts(Integer cus_id) {
        List<Product> list = new ArrayList<>();
        String sql;

        if (cus_id != null) {
            sql = "SELECT TOP 1 o.order_id FROM [Order] o WHERE o.cus_id = ? ORDER BY o.order_date DESC";
            try {
                conn = new DBcontext().getConnection();
                ps = conn.prepareStatement(sql);
                ps.setInt(1, cus_id);
                rs = ps.executeQuery();

                if (rs.next()) {
                    sql = "SELECT TOP 50 p.pro_id, p.pro_name, p.image, p.discount, p.price, (p.price * (1 - p.discount / 100.0)) AS discounted_price FROM Product p WHERE p.type_id IN ("
                            + "    SELECT DISTINCT pr.type_id "
                            + "    FROM [Order] o "
                            + "    JOIN OrderDetail od ON o.order_id = od.order_id "
                            + "    JOIN Product pr ON od.pro_id = pr.pro_id "
                            + "    WHERE o.cus_id = ? "
                            + ") "
                            + "AND p.status = 'active' ORDER BY p.pro_id DESC";
                } else {
                    sql = "SELECT TOP 1 c.cart_id FROM Cart c WHERE c.cus_id = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, cus_id);
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        sql = "SELECT TOP 50 p.pro_id, p.pro_name, p.image, p.discount, p.price, (p.price * (1 - p.discount / 100.0)) AS discounted_price FROM Product p WHERE p.type_id IN ("
                                + "    SELECT DISTINCT pr.type_id "
                                + "    FROM Cart c "
                                + "    JOIN Product pr ON c.pro_id = pr.pro_id "
                                + "    WHERE c.cus_id = ? "
                                + ") "
                                + "AND p.status = 'active' ORDER BY p.pro_id DESC";
                    } else {
                        sql = "WITH TopType AS ("
                                + "    SELECT TOP 3 p.type_id "
                                + "    FROM OrderDetail od "
                                + "    JOIN Product p ON od.pro_id = p.pro_id "
                                + "    GROUP BY p.type_id "
                                + "    ORDER BY SUM(od.quantity) DESC"
                                + ") "
                                + "SELECT TOP 50 p.pro_id, p.pro_name, p.image, p.discount, p.price, (p.price * (1 - p.discount / 100.0)) AS discounted_price FROM Product p "
                                + "WHERE p.type_id IN (SELECT type_id FROM TopType) "
                                + "AND p.status = 'active'";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            sql = "WITH TopType AS ("
                    + "    SELECT TOP 3 p.type_id "
                    + "    FROM OrderDetail od "
                    + "    JOIN Product p ON od.pro_id = p.pro_id "
                    + "    GROUP BY p.type_id "
                    + "    ORDER BY SUM(od.quantity) DESC"
                    + ") "
                    + "SELECT TOP 50 p.pro_id, p.pro_name, p.image, p.discount, p.price, (p.price * (1 - p.discount / 100.0)) AS discounted_price FROM Product p "
                    + "WHERE p.type_id IN (SELECT type_id FROM TopType) "
                    + "AND p.status = 'active'";
        }

        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            if (cus_id != null) {
                ps.setInt(1, cus_id);
            }
            rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setPro_id(rs.getInt(1));
                p.setPro_name(rs.getString(2));
                p.setImage(rs.getString(3));
                p.setDiscount(rs.getInt(4));
                p.setPrice(rs.getBigDecimal(5));
                p.setDiscountedPrice(rs.getBigDecimal(6));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
