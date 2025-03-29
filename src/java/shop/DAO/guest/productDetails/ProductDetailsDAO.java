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
                + "       CAST(COALESCE((SELECT AVG(CAST(rating AS DECIMAL(2,1))) FROM Feedback WHERE pro_id = p.pro_id), 0) AS DECIMAL(10,2)) AS averageRating, "
                + "       COALESCE((SELECT COUNT(*) FROM Feedback WHERE pro_id = p.pro_id), 0) AS feedbackCount "
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
                pd.setSize(rs.getString("sizes"));
                Type type = new Type();
                type.setType_id(rs.getInt("type_id"));
                type.setType_name(rs.getString("type_name"));
                pd.setType(type);
                pd.setStock(rs.getInt("total_stock"));
                pd.setDiscount(rs.getInt("discount"));
                pd.setPrice(rs.getBigDecimal("original_price"));
                pd.setDiscountedPrice(rs.getBigDecimal("discounted_price"));
                pd.setAverageRating(rs.getDouble("averageRating"));
                pd.setFeedbackCount(rs.getInt("feedbackCount"));
                return pd;
            }
        } catch (SQLException e) {
            System.out.println("Error getting product details: " + e.getMessage());
        } finally {
            closeResources();
        }
        return null;
    }

    public boolean addFeedback(String customerId, String productId, int rating, String comment) {
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
