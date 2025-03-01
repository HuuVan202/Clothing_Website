package shop.DAO.admin.productManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import shop.context.DBcontext;
import shop.model.Product;
import shop.model.Type;

public class AdminProductsDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public List<Product> getProductsByPage(int start, int limit) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.pro_id, p.pro_name, p.image, p.size, p.gender, p.brand, " +
                     "t.type_name, p.price, p.discount, p.stock, p.status " +
                     "FROM Product p " +
                     "JOIN Type t ON p.type_id = t.type_id " +
                     "ORDER BY pro_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, start);
            ps.setInt(2, limit);
            rs = ps.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setPro_id(rs.getInt("pro_id"));
                p.setPro_name(rs.getString("pro_name"));
                p.setImage(rs.getString("image"));
                p.setSize(rs.getString("size"));
                p.setGender(rs.getString("gender"));
                p.setBrand(rs.getString("brand"));
                
                Type type = new Type();
                type.setType_name(rs.getString("type_name"));
                p.setType(type);
                
                p.setPrice(rs.getBigDecimal("price"));
                p.setDiscount(rs.getInt("discount"));
                p.setStock(rs.getInt("stock"));
                p.setStatus(rs.getString("status"));

                productList.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }

    public int getTotalProductCount() {
        String sql = "SELECT COUNT(*) FROM Product";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getMaxPageDisplay() {
        return 10; // Hiển thị tối đa 10 trang
    }
}
