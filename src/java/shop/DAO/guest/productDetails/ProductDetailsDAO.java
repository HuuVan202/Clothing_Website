package shop.DAO.guest.productDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import shop.context.DBcontext;
import shop.model.Product;
import shop.model.Feedback;
import shop.model.Type;

public class ProductDetailsDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public Product getProductDetails(int pro_id) {
        String sql = "SELECT p.image, p.pro_id, p.pro_name, p.size, p.type_id, "
                + "       t.type_name, p.stock, p.discount, p.price AS original_price, "
                + "       CASE "
                + "           WHEN p.discount > 0 THEN p.price * (1 - p.discount / 100.0) "
                + "           ELSE p.price "
                + "       END AS discounted_price, "
                + "       COALESCE((SELECT AVG(rating) FROM Feedback WHERE pro_id = p.pro_id), 0) AS averageRating, "
                + "       COALESCE((SELECT COUNT(*) FROM Feedback WHERE pro_id = p.pro_id), 0) AS feedbackCount "
                + "FROM Product p "
                + "JOIN Type t ON p.type_id = t.type_id " // Join với bảng Type để lấy type_name
                + "WHERE p.pro_id = ? AND p.status = 'active'";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, pro_id); // Truyền tham số ID vào câu SQL
            rs = ps.executeQuery();
            if (rs.next()) {
                Product pd = new Product();
                pd.setImage(rs.getString("image"));
                pd.setPro_id(rs.getInt("pro_id"));
                pd.setPro_name(rs.getString("pro_name"));
                pd.setSize(rs.getString("size"));
                Type type = new Type();
                type.setType_id(rs.getInt("type_id"));
                type.setType_name(rs.getString("type_name"));
                pd.setType(type);
                pd.setStock(rs.getInt("stock"));
                pd.setDiscount(rs.getInt("discount"));
                pd.setPrice(rs.getBigDecimal("original_price"));
                pd.setDiscountedPrice(rs.getBigDecimal("discounted_price"));
                pd.setAverageRating(rs.getDouble("averageRating"));
                pd.setFeedbackCount(rs.getInt("feedbackCount"));
                return pd; // Trả về sản phẩm nếu tìm thấy
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy sản phẩm
    }

    public List<Feedback> getFeedBackofProduct(int pro_id) {
        List<Feedback> feedback = new ArrayList<>();
        String sql = "SELECT f.feedback_id, f.pro_id, c.cus_name, f.rating, f.comment, f.feedback_date "
                + "FROM Feedback f "
                + "JOIN Customer c ON f.cus_id = c.cus_id "
                + "WHERE f.pro_id = ?";

        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, pro_id);
            rs = ps.executeQuery();

            while (rs.next()) {
                Feedback f = new Feedback();
                f.setFeedback_id(rs.getInt("feedback_id"));
                f.setPro_id(rs.getInt("pro_id"));
                f.setCus_name(rs.getString("cus_name"));
                f.setRating(rs.getInt("rating"));
                f.setComment(rs.getString("comment"));
                f.setFeedback_date(rs.getDate("feedback_date"));

                feedback.add(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedback;
    }

    public List<Product> getSuggestProducts(int pro_id) {
        List<Product> list = new ArrayList<>();
        String sql = "WITH TypeProducts AS ( "
                + "    SELECT p.pro_id, p.pro_name, p.image, p.discount, p.price, "
                + "           (p.price * (1 - p.discount / 100.0)) AS discounted_price, "
                + "           SUM(od.quantity) AS total_order "
                + "    FROM Product p "
                + "    JOIN OrderDetail od ON p.pro_id = od.pro_id "
                + "    JOIN [Order] o ON od.order_id = o.order_id "
                + "    WHERE p.type_id = (SELECT type_id FROM Product WHERE pro_id = ?) "
                + "    AND p.pro_id != ? " // Loại bỏ sản phẩm hiện tại khỏi danh sách gợi ý
                + "    AND p.status = 'active' "
                + "    GROUP BY p.pro_id, p.pro_name, p.image, p.discount, p.price "
                + ") "
                + "SELECT TOP 5 * FROM TypeProducts ORDER BY total_order DESC";

        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, pro_id); // Lấy type_id của sản phẩm hiện tại
            ps.setInt(2, pro_id); // Không lấy lại chính sản phẩm đang xem
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
//        String sql;
//
//        if (cus_id != null) {
//            sql = "SELECT TOP 1 o.order_id FROM [Order] o WHERE o.cus_id = ? ORDER BY o.order_date DESC";
//            try {
//                conn = new DBcontext().getConnection();
//                ps = conn.prepareStatement(sql);
//                ps.setInt(1, cus_id);
//                rs = ps.executeQuery();
//
//                if (rs.next()) {
//                    sql = "SELECT TOP 5 p.pro_id, p.pro_name, p.image, p.discount, p.price, (p.price * (1 - p.discount / 100.0)) AS discounted_price FROM Product p WHERE p.type_id IN ("
//                            + "    SELECT DISTINCT pr.type_id "
//                            + "    FROM [Order] o "
//                            + "    JOIN OrderDetail od ON o.order_id = od.order_id "
//                            + "    JOIN Product pr ON od.pro_id = pr.pro_id "
//                            + "    WHERE o.cus_id = ? "
//                            + ") "
//                            + "AND p.status = 'active' ORDER BY p.pro_id DESC";
//                } else {
//                    sql = "SELECT TOP 1 c.cart_id FROM Cart c WHERE c.cus_id = ?";
//                    ps = conn.prepareStatement(sql);
//                    ps.setInt(1, cus_id);
//                    rs = ps.executeQuery();
//
//                    if (rs.next()) {
//                        sql = "SELECT TOP 5 p.pro_id, p.pro_name, p.image, p.discount, p.price, (p.price * (1 - p.discount / 100.0)) AS discounted_price FROM Product p WHERE p.type_id IN ("
//                                + "    SELECT DISTINCT pr.type_id "
//                                + "    FROM Cart c "
//                                + "    JOIN Product pr ON c.pro_id = pr.pro_id "
//                                + "    WHERE c.cus_id = ? "
//                                + ") "
//                                + "AND p.status = 'active' ORDER BY p.pro_id DESC";
//                    } else {
//                        sql = "WITH TopType AS ("
//                                + "    SELECT TOP 3 p.type_id "
//                                + "    FROM OrderDetail od "
//                                + "    JOIN Product p ON od.pro_id = p.pro_id "
//                                + "    GROUP BY p.type_id "
//                                + "    ORDER BY SUM(od.quantity) DESC"
//                                + ") "
//                                + "SELECT TOP 5 p.pro_id, p.pro_name, p.image, p.discount, p.price, (p.price * (1 - p.discount / 100.0)) AS discounted_price FROM Product p "
//                                + "WHERE p.type_id IN (SELECT type_id FROM TopType) "
//                                + "AND p.status = 'active'";
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            sql = "WITH TopType AS ("
//                    + "    SELECT TOP 3 p.type_id "
//                    + "    FROM OrderDetail od "
//                    + "    JOIN Product p ON od.pro_id = p.pro_id "
//                    + "    GROUP BY p.type_id "
//                    + "    ORDER BY SUM(od.quantity) DESC"
//                    + ") "
//                    + "SELECT TOP 5 p.pro_id, p.pro_name, p.image, p.discount, p.price, (p.price * (1 - p.discount / 100.0)) AS discounted_price FROM Product p "
//                    + "WHERE p.type_id IN (SELECT type_id FROM TopType) "
//                    + "AND p.status = 'active'";
//        }
//
//        try {
//            conn = new DBcontext().getConnection();
//            ps = conn.prepareStatement(sql);
//            if (cus_id != null) {
//                ps.setInt(1, cus_id);
//            }
//            rs = ps.executeQuery();

    public static void main(String[] args) {
        ProductDetailsDAO p = new ProductDetailsDAO();
        p.getSuggestProducts(1);
    }

}
