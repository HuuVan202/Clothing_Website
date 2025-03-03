package shop.DAO.admin.productManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import shop.context.DBcontext;
import shop.model.Product;
import shop.model.Type;

public class productManagementDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public List<Product> getProductsByPage(int start, int limit) {
        List<Product> productList = new ArrayList<>();
        if (start < 0) {
            start = 0; // Đảm bảo OFFSET không bị âm
        }
        String sql = "SELECT p.pro_id, p.pro_name, p.image, COALESCE(p.size, '') AS size, p.gender, p.brand, "
                + "p.type_id, t.type_name, p.price, p.discount, p.stock, p.status "
                + "FROM Product p "
                + "JOIN Type t ON p.type_id = t.type_id "
                + "ORDER BY pro_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
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
                type.setType_id(rs.getInt("type_id"));
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

    public List<Type> getAllTypes() {
        List<Type> typeList = new ArrayList<>();
        String sql = "SELECT type_id, type_name FROM Type";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Type t = new Type();
                t.setType_id(rs.getInt("type_id"));
                t.setType_name(rs.getString("type_name"));
                typeList.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return typeList;
    }

    public int getTypeIdByName(String typeName) {
        String sql = "SELECT type_id FROM Type WHERE type_name = ?";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, typeName);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("type_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }

    public boolean addProduct(Product product) {
        String sql = "INSERT INTO Product (pro_name, image, size, gender, brand, type_id, price, discount, stock, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getPro_name());
            ps.setString(2, product.getImage());
            ps.setString(3, product.getSize());
            ps.setString(4, product.getGender());
            ps.setString(5, product.getBrand());
            ps.setInt(6, product.getType().getType_id());
            ps.setBigDecimal(7, product.getPrice());
            ps.setInt(8, product.getDiscount());
            ps.setInt(9, product.getStock());
            ps.setString(10, product.getStatus());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Chỉ trả về true nếu có dòng được chèn

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Nếu có lỗi, trả về false
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Product getProductById(int productId) {
        String sql = "SELECT p.pro_id, p.pro_name, p.image, COALESCE(p.size, '') AS size, p.gender, p.brand, "
                + "p.type_id, t.type_name, p.price, p.discount, p.stock, p.status "
                + "FROM Product p "
                + "JOIN Type t ON p.type_id = t.type_id "
                + "WHERE p.pro_id = ?";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            rs = ps.executeQuery();

            if (rs.next()) {
                Product p = new Product();
                p.setPro_id(rs.getInt("pro_id"));
                p.setPro_name(rs.getString("pro_name"));
                p.setImage(rs.getString("image"));
                p.setSize(rs.getString("size"));
                p.setGender(rs.getString("gender"));
                p.setBrand(rs.getString("brand"));

                Type type = new Type();
                type.setType_id(rs.getInt("type_id"));
                type.setType_name(rs.getString("type_name"));
                p.setType(type);

                p.setPrice(rs.getBigDecimal("price"));
                p.setDiscount(rs.getInt("discount"));
                p.setStock(rs.getInt("stock"));
                p.setStatus(rs.getString("status"));

                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE Product SET pro_name = ?, image = ?, size = ?, gender = ?, "
                + "brand = ?, type_id = ?, price = ?, discount = ?, stock = ?, status = ? "
                + "WHERE pro_id = ?";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getPro_name());
            ps.setString(2, product.getImage());
            ps.setString(3, product.getSize());
            ps.setString(4, product.getGender());
            ps.setString(5, product.getBrand());
            ps.setInt(6, product.getType().getType_id());
            ps.setBigDecimal(7, product.getPrice());
            ps.setInt(8, product.getDiscount());
            ps.setInt(9, product.getStock());
            ps.setString(10, product.getStatus());
            ps.setInt(11, product.getPro_id());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    public static void main(String[] args) {
//        AdminProductsDAO p = new AdminProductsDAO();
//        p.getAllTypes();
//    }
}
