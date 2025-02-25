package shop.DAO.guest.productDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import shop.context.DBcontext;
import shop.model.Product;

public class ProductDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    public Product getProductById(int id) {
        String sql = "SELECT * FROM Product WHERE pro_id = ?";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Product(
                    rs.getInt("pro_id"),
                    rs.getString("pro_name"),
                    rs.getBigDecimal("price"),
                    rs.getInt("stock"),
                    rs.getString("image"),
                    rs.getString("size"),
                    rs.getString("gender"),
                    rs.getString("brand"),
                    rs.getInt("type_id"),
                    rs.getString("status"),
                    rs.getInt("discount")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Product> getSuggestedProducts(int categoryId, int excludeId) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT TOP 5 * FROM Product WHERE type_id = ? AND pro_id <> ?";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            ps.setInt(2, excludeId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                    rs.getInt("pro_id"),
                    rs.getString("pro_name"),
                    rs.getBigDecimal("price"),
                    rs.getInt("stock"),
                    rs.getString("image"),
                    rs.getString("size"),
                    rs.getString("gender"),
                    rs.getString("brand"),
                    rs.getInt("type_id"),
                    rs.getString("status"),
                    rs.getInt("discount")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
//    public static void main(String[] args) {
//        ProductDAO dao = new ProductDAO();
//        dao.getSuggestedProducts(1, 6);
//    }
//            
}