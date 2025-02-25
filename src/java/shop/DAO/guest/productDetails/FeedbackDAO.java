/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package shop.DAO.guest.productDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import shop.context.DBcontext;
import shop.model.Feedback;

public class FeedbackDAO {
    
    public List<Feedback> getFeedbackByProductId(int productId) {
        List<Feedback> feedbacks = new ArrayList<>();
        String sql = "SELECT f.feedback_id, f.pro_id, f.cus_id, c.cus_name, f.rating, f.comment, f.feedback_date FROM Feedback f JOIN Customer c ON f.cus_id = c.cus_id WHERE f.pro_id = ?";
        
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                feedbacks.add(new Feedback(
                    rs.getInt("feedback_id"),
                    rs.getInt("pro_id"),
                    rs.getInt("cus_id"),
                    rs.getString("cus_name"),  
                    rs.getInt("rating"),
                    rs.getString("comment"),
                    rs.getDate("feedback_date")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedbacks;
      
    }
    public double getAverageRating(int productId) {
        String sql = "SELECT AVG(rating) AS avg_rating FROM Feedback WHERE pro_id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    public int getFeedbackCount(int productId) {
        String sql = "SELECT COUNT(*) AS count FROM Feedback WHERE pro_id = ?";
        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    
//        public static void main(String[] args) {
//        FeedbackDAO dao = new FeedbackDAO();
//        dao.getAverageRating(1);
//    }
// 
}