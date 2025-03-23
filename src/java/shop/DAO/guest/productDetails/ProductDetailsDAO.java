package shop.DAO.guest.productDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import shop.context.DBcontext;
import shop.model.Product;
import shop.model.Feedback;
import shop.model.Type;

public class ProductDetailsDAO {

    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = new DBcontext().getConnection();
        }
        return conn;
    }

    public Product getProductDetails(int pro_id) {
        String sql = "SELECT p.image, p.pro_id, p.pro_name, p.size, p.type_id, "
                + "       t.type_name, p.stock, p.discount, p.price AS original_price, "
                + "       CASE "
                + "           WHEN p.discount > 0 THEN p.price * (1 - p.discount / 100.0) "
                + "           ELSE p.price "
                + "       END AS discounted_price, "
                + "       CAST(COALESCE((SELECT AVG(CAST(rating AS DECIMAL(2,1))) FROM Feedback WHERE pro_id = p.pro_id), 0) AS DECIMAL(10,2)) AS averageRating, "
                + "       COALESCE((SELECT COUNT(*) FROM Feedback WHERE pro_id = p.pro_id), 0) AS feedbackCount "
                + "FROM Product p "
                + "JOIN Type t ON p.type_id = t.type_id " // Join với bảng Type để lấy type_name
                + "WHERE p.pro_id = ? AND p.status = 'active'";
        try {
            conn = getConnection();
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
        } catch (SQLException e) {
            System.out.println("Error getting product details: " + e.getMessage());
        } finally {
            closeResources();
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
            conn = getConnection();
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
        } catch (SQLException e) {
            System.out.println("Error getting feedback: " + e.getMessage());
        } finally {
            closeResources();
        }
        return feedback;
    }
    
    public List<Product> getSuggestProducts(Integer pro_id) {
    List<Product> list = new ArrayList<>();
    String sql = 
        "WITH OrderedProducts AS ( " +
        "    SELECT TOP 5 p.pro_id, p.pro_name, p.image, p.discount, p.price, " +
        "           (p.price * (1 - p.discount / 100.0)) AS discounted_price, " +
        "           COALESCE(SUM(od.quantity), 0) AS total_order " + // Xử lý nếu không có order thì mặc định là 0
        "    FROM Product p " +
        "    LEFT JOIN OrderDetail od ON p.pro_id = od.pro_id " + 
        "    WHERE p.type_id = (SELECT type_id FROM Product WHERE pro_id = ?) " +
        "    AND p.pro_id != ? " + // Không lấy lại chính sản phẩm đang xem
        "    AND p.status = 'active' " +
        "    GROUP BY p.pro_id, p.pro_name, p.image, p.discount, p.price " +
        "    ORDER BY total_order DESC " + // Sắp xếp theo số lượng đặt hàng nhiều nhất
        "), " +
        "AdditionalProducts AS ( " +
        "    SELECT TOP (5 - (SELECT COUNT(*) FROM OrderedProducts)) " + // Chỉ lấy thêm sản phẩm nếu thiếu
        "           p.pro_id, p.pro_name, p.image, p.discount, p.price, " +
        "           (p.price * (1 - p.discount / 100.0)) AS discounted_price, " +
        "           0 AS total_order " + // Các sản phẩm này không có order
        "    FROM Product p " +
        "    WHERE p.type_id = (SELECT type_id FROM Product WHERE pro_id = ?) " +
        "    AND p.pro_id NOT IN (SELECT pro_id FROM OrderedProducts) " + // Không lặp lại sản phẩm đã có order
        "    AND p.pro_id != ? " + // Không lấy lại chính sản phẩm đang xem
        "    AND p.status = 'active' " +
        "    ORDER BY p.pro_id DESC " + // Lấy sản phẩm mới nhất trước
        ") " +
        "SELECT * FROM OrderedProducts " +
        "UNION ALL " +
        "SELECT * FROM AdditionalProducts"; // Kết hợp hai bảng OrderedProducts & AdditionalProducts

    try {
        conn = getConnection();
        ps = conn.prepareStatement(sql);
        ps.setInt(1, pro_id); // Lấy type_id của sản phẩm hiện tại
        ps.setInt(2, pro_id); // Không lấy lại chính sản phẩm
        ps.setInt(3, pro_id); // Lấy thêm sản phẩm cùng type_id nếu thiếu
        ps.setInt(4, pro_id); // Không lấy lại chính sản phẩm

        rs = ps.executeQuery();
        while (rs.next()) {
            Product p = new Product();
            p.setPro_id(rs.getInt("pro_id"));
            p.setPro_name(rs.getString("pro_name"));
            p.setImage(rs.getString("image"));
            p.setDiscount(rs.getInt("discount"));
            p.setPrice(rs.getBigDecimal("price"));
            p.setDiscountedPrice(rs.getBigDecimal("discounted_price"));
            list.add(p);
        }
    } catch (SQLException e) {
        System.out.println("Error getting suggested products: " + e.getMessage());
    } finally {
        closeResources();
    }
    return list;
}
    
    public boolean hasCustomerGivenFeedback(int customerId, int productId) {
        String sql = "SELECT COUNT(*) as count FROM Feedback f " +
                    "WHERE f.cus_id = ? AND f.pro_id = ?";
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setInt(2, productId);
            
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking customer feedback: " + e.getMessage());
        } finally {
            closeResources();
        }
        return false;
    }
    
    public boolean canCustomerGiveFeedback(int customerId, int productId) {
        String sql = "SELECT DISTINCT o.order_id FROM [Order] o " +
                    "INNER JOIN OrderDetail od ON o.order_id = od.order_id " +
                    "WHERE o.cus_id = ? AND od.pro_id = ? AND o.tracking = 'delivered' " +
                    "AND NOT EXISTS (SELECT 1 FROM Feedback f WHERE f.cus_id = ? AND f.pro_id = ?)";
        
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setInt(2, productId);
            ps.setInt(3, customerId);
            ps.setInt(4, productId);
            
            rs = ps.executeQuery();
            return rs.next(); // If there's at least one delivered order without feedback
        } catch (SQLException e) {
            System.out.println("Error checking feedback eligibility: " + e.getMessage());
        } finally {
            closeResources();
        }
        return false;
    }
    
    public boolean addFeedback(String customerId, String productId, int rating, String comment) {
        // First check if customer can give feedback
        int cusId = Integer.parseInt(customerId);
        int proId = Integer.parseInt(productId);
        
        if (!canCustomerGiveFeedback(cusId, proId)) {
            return false;
        }
        
        String sql = "INSERT INTO Feedback (cus_id, pro_id, rating, comment, feedback_date) VALUES (?, ?, ?, ?, GETDATE())";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, cusId);
            ps.setInt(2, proId);
            ps.setInt(3, rating);
            ps.setString(4, comment);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error adding feedback: " + e.getMessage());
        } finally {
            closeResources();
        }
        return false;
    }
    
    public String getCusIdByUsername(String username) {
        String sql = "SELECT cus_id FROM Customer WHERE username = ?";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return String.valueOf(rs.getInt("cus_id"));
            }
        } catch (SQLException e) {
            System.out.println("Error getting customer ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return null;
    }
    
    public boolean hasCustomerReviewedProduct(String customerId, String productId) {
        String sql = "SELECT COUNT(*) FROM Feedback WHERE cus_id = ? AND pro_id = ?";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, customerId);
            ps.setString(2, productId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking customer feedback: " + e.getMessage());
        } finally {
            closeResources();
        }
        return false;
    }
}
