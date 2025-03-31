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
import shop.model.ProductSize;
import shop.model.Type;

public class ProductDetailsDAO {

    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    private void closeResources() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
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
        String sql = "SELECT p.image, p.pro_id, p.pro_name, p.type_id, "
                + "       t.type_name, p.discount, p.price AS original_price, "
                + "       STRING_AGG(ps.size, ', ') AS sizes, "
                + "       SUM(ps.stock) AS total_stock, "
                + "       CASE "
                + "           WHEN p.discount > 0 THEN p.price * (1 - p.discount / 100.0) "
                + "           ELSE p.price "
                + "       END AS discounted_price, "
                + "       CAST(COALESCE((SELECT AVG(CAST(rating AS DECIMAL(2,1))) FROM Feedback WHERE pro_id = p.pro_id), 0) AS DECIMAL(2,1)) AS averageRating, "
                + "       COALESCE((SELECT COUNT(*) FROM Feedback WHERE pro_id = p.pro_id), 0) AS feedbackCount, "
                + "       COALESCE((SELECT SUM(od.quantity) FROM OrderDetail od WHERE od.pro_id = p.pro_id), 0) AS soldProduct "
                + "FROM Product p "
                + "JOIN Type t ON p.type_id = t.type_id "
                + "LEFT JOIN ProductSize ps ON p.pro_id = ps.pro_id "
                + "WHERE p.pro_id = ? AND p.status = 'active' "
                + "GROUP BY p.image, p.pro_id, p.pro_name, p.type_id, t.type_name, p.discount, p.price";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, pro_id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Product pd = new Product();
                pd.setImage(rs.getString("image"));
                pd.setPro_id(rs.getInt("pro_id"));
                pd.setPro_name(rs.getString("pro_name"));
                pd.setSize(rs.getString("sizes")); // Now getting concatenated sizes
                Type type = new Type();
                type.setType_id(rs.getInt("type_id"));
                type.setType_name(rs.getString("type_name"));
                pd.setType(type);
                pd.setStock(rs.getInt("total_stock")); // Now getting sum of all sizes' stock
                pd.setDiscount(rs.getInt("discount"));
                pd.setPrice(rs.getBigDecimal("original_price"));
                pd.setDiscountedPrice(rs.getBigDecimal("discounted_price"));
                pd.setAverageRating(rs.getDouble("averageRating"));
                pd.setFeedbackCount(rs.getInt("feedbackCount"));
                pd.setSoldProduct(rs.getInt("soldProduct"));
                return pd;
            }
        } catch (SQLException e) {
            System.out.println("Error getting product details: " + e.getMessage());
        } finally {
            closeResources();
        }
        return null;
    }

    public List<Feedback> getFilteredFeedbackOfProduct(int pro_id, String filterType) {
        List<Feedback> feedback = new ArrayList<>();

        String baseSql = """
        SELECT 
            f.feedback_id, 
            f.pro_id, 
            f.cus_id,
            c.cus_name, 
            f.rating, 
            f.comment, 
            f.feedback_date,
            (
                SELECT STRING_AGG(od.size, ', ')
                FROM [Order] o
                JOIN OrderDetail od ON o.order_id = od.order_id
                WHERE o.cus_id = f.cus_id 
                  AND od.pro_id = f.pro_id 
                  AND o.tracking = 'delivered'
            ) AS purchasedSizes
        FROM Feedback f
        JOIN Customer c ON f.cus_id = c.cus_id
        WHERE f.pro_id = ?
    """;

        switch (filterType) {
            case "5star":
                baseSql += " AND f.rating = 5 ORDER BY f.feedback_date DESC";
                break;
            case "4star":
                baseSql += " AND f.rating = 4 ORDER BY f.feedback_date DESC";
                break;
            case "3star":
                baseSql += " AND f.rating = 3 ORDER BY f.feedback_date DESC";
                break;
            case "2star":
                baseSql += " AND f.rating = 2 ORDER BY f.feedback_date DESC";
                break;
            case "1star":
                baseSql += " AND f.rating = 1 ORDER BY f.feedback_date DESC";
                break;
            case "oldest":
                baseSql += " ORDER BY f.feedback_date ASC";
                break;
            case "all":
            default:
                baseSql += " ORDER BY f.rating DESC, f.feedback_date DESC";
                break;
        }

        try {
            conn = getConnection();
            ps = conn.prepareStatement(baseSql);
            ps.setInt(1, pro_id);
            rs = ps.executeQuery();

            while (rs.next()) {
                Feedback f = new Feedback();
                f.setFeedback_id(rs.getInt("feedback_id"));
                f.setPro_id(rs.getInt("pro_id"));
                f.setCus_id(rs.getInt("cus_id"));
                f.setCus_name(rs.getString("cus_name"));
                f.setRating(rs.getInt("rating"));
                f.setComment(rs.getString("comment"));
                f.setFeedback_date(rs.getDate("feedback_date"));
                f.setPurchasedSizes(rs.getString("purchasedSizes"));
                feedback.add(f);
            }
        } catch (SQLException e) {
            System.out.println("Error getting filtered feedback: " + e.getMessage());
        } finally {
            closeResources();
        }
        return feedback;
    }
    
    public Feedback getFeedbackByCustomer(String customerId, String productId) {
        String sql = "SELECT f.feedback_id, f.pro_id, f.rating, f.comment, f.feedback_date, "
                + "(SELECT STRING_AGG(od.size, ', ') FROM [Order] o JOIN OrderDetail od ON o.order_id = od.order_id "
                + "WHERE o.cus_id = f.cus_id AND od.pro_id = f.pro_id AND o.tracking = 'delivered') AS purchasedSizes "
                + "FROM Feedback f WHERE f.cus_id = ? AND f.pro_id = ?";

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, customerId);
            ps.setString(2, productId);
            rs = ps.executeQuery();

            if (rs.next()) {
                Feedback fb = new Feedback();
                fb.setFeedback_id(rs.getInt("feedback_id"));
                fb.setPro_id(rs.getInt("pro_id"));
                fb.setRating(rs.getInt("rating"));
                fb.setComment(rs.getString("comment"));
                fb.setFeedback_date(rs.getDate("feedback_date"));
                fb.setPurchasedSizes(rs.getString("purchasedSizes"));
                return fb;
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving customer feedback: " + e.getMessage());
        } finally {
            closeResources();
        }
        return null;
    }

    public List<Product> getSuggestProducts(int pro_id) {
        List<Product> list = new ArrayList<>();
        String sql
                = "WITH CurrentProduct AS ( "
                + "    SELECT pro_id, price, discount, "
                + "           (price * (1 - discount / 100.0)) AS current_discounted_price, "
                + "           type_id "
                + "    FROM Product "
                + "    WHERE pro_id = ? "
                + "), "
                + "FilteredProducts AS ( "
                + "    SELECT TOP 10 p.pro_id, p.pro_name, p.image, p.discount, p.price, "
                + "           (p.price * (1 - p.discount / 100.0)) AS discounted_price, "
                + "           COALESCE(SUM(od.quantity), 0) AS soldProduct, "
                + "           CAST(COALESCE((SELECT AVG(CAST(rating AS DECIMAL(2,1))) "
                + "                          FROM Feedback f WHERE f.pro_id = p.pro_id), 0) AS DECIMAL(2,1)) AS averageRating "
                + "    FROM Product p "
                + "    LEFT JOIN OrderDetail od ON p.pro_id = od.pro_id, "
                + "         CurrentProduct cp "
                + "    WHERE p.pro_id != cp.pro_id "
                + "      AND p.type_id = cp.type_id "
                + "      AND p.status = 'active' "
                + "      AND (p.price * (1 - p.discount / 100.0)) BETWEEN cp.current_discounted_price * 0.8 AND cp.current_discounted_price * 1.2 "
                + "    GROUP BY p.pro_id, p.pro_name, p.image, p.discount, p.price "
                + "    ORDER BY soldProduct DESC, averageRating DESC, p.pro_id DESC "
                + "), "
                + "AdditionalProducts AS ( "
                + "    SELECT TOP (10 - (SELECT COUNT(*) FROM FilteredProducts)) "
                + "           p.pro_id, p.pro_name, p.image, p.discount, p.price, "
                + "           (p.price * (1 - p.discount / 100.0)) AS discounted_price, "
                + "           0 AS soldProduct, "
                + "           CAST(COALESCE((SELECT AVG(CAST(rating AS DECIMAL(2,1))) "
                + "                          FROM Feedback f WHERE f.pro_id = p.pro_id), 0) AS DECIMAL(2,1)) AS averageRating "
                + "    FROM Product p, CurrentProduct cp "
                + "    WHERE p.pro_id != cp.pro_id "
                + "      AND p.type_id = cp.type_id "
                + "      AND p.status = 'active' "
                + "      AND p.pro_id NOT IN (SELECT pro_id FROM FilteredProducts) "
                + "    ORDER BY p.pro_id DESC "
                + ") "
                + "SELECT * FROM FilteredProducts "
                + "UNION ALL "
                + "SELECT * FROM AdditionalProducts";

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, pro_id); // CurrentProduct

            rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setPro_id(rs.getInt("pro_id"));
                p.setPro_name(rs.getString("pro_name"));
                p.setImage(rs.getString("image"));
                p.setDiscount(rs.getInt("discount"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setDiscountedPrice(rs.getBigDecimal("discounted_price"));
                p.setAverageRating(rs.getDouble("averageRating"));
                p.setSoldProduct(rs.getInt("soldProduct")); // ✅ mới thêm
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error getting suggested products: " + e.getMessage());
        } finally {
            closeResources();
        }
        return list;
    }

    public boolean canCustomerGiveFeedback(int customerId, int productId) {
        String sql = "SELECT DISTINCT o.order_id FROM [Order] o "
                + "INNER JOIN OrderDetail od ON o.order_id = od.order_id "
                + "WHERE o.cus_id = ? AND od.pro_id = ? AND o.tracking = 'delivered' "
                + "AND NOT EXISTS (SELECT 1 FROM Feedback f WHERE f.cus_id = ? AND f.pro_id = ?)";

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
    
    public boolean updateFeedback(int feedbackId, int rating, String comment) {
        String sql = "UPDATE Feedback SET rating = ?, comment = ?, feedback_date = GETDATE() WHERE feedback_id = ?";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, rating);
            ps.setString(2, comment);
            ps.setInt(3, feedbackId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating feedback: " + e.getMessage());
        } finally {
            closeResources();
        }
        return false;
    }

    public List<ProductSize> getSizeByProductId(int proId) {
        List<ProductSize> sizes = new ArrayList<>();
        String sql = "SELECT ps.size_id, ps.pro_id, ps.size, ps.stock "
                + "FROM ProductSize ps "
                + "WHERE ps.pro_id = ?";

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, proId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                sizes.add(new ProductSize(
                        resultSet.getInt("size_id"),
                        resultSet.getInt("pro_id"),
                        resultSet.getString("size"),
                        resultSet.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sizes;
    }
}
