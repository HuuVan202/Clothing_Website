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

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.pro_id, p.pro_name, p.image, p.size, p.gender, p.brand, "
                + "t.type_name, p.price, p.discount, p.stock, p.status "
                + "FROM Product p "
                + "JOIN Type t ON p.type_id = t.type_id "
                + "ORDER BY pro_id ASC";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
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
    
    public static void main(String[] args) {
        AdminProductsDAO p = new AdminProductsDAO();
        p.getAllProducts();
    }
}
