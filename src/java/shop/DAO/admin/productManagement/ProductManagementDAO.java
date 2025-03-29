package shop.DAO.admin.productManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import shop.context.DBcontext;
import shop.model.Product;
import shop.model.ProductSize;
import shop.model.Type;

public class ProductManagementDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

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

    public List<Product> getFilteredAndSortedProducts(String typeFilter, String genderFilter, String brandFilter,
            String statusFilter, String stockFilter, String searchQuery, String sortBy, int page, int itemsPerPage) {
        List<Product> products = new ArrayList<>();
        
        // First query to get basic product information
        StringBuilder sql = new StringBuilder(
                "SELECT p.pro_id, p.pro_name, p.image, "
                + "p.gender, p.brand, p.type_id, t.type_name, p.price, p.discount, "
                + "p.status "
                + "FROM Product p "
                + "JOIN Type t ON p.type_id = t.type_id "
                + "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        // Add filter conditions
        if (genderFilter != null && !genderFilter.isEmpty()) {
            sql.append("AND LOWER(p.gender) = LOWER(?) ");
            params.add(genderFilter);
        }
        if (typeFilter != null && !typeFilter.isEmpty()) {
            sql.append("AND LOWER(t.type_name) = LOWER(?) ");
            params.add(typeFilter);
        }
        if (brandFilter != null && !brandFilter.isEmpty()) {
            sql.append("AND LOWER(p.brand) = LOWER(?) ");
            params.add(brandFilter);
        }
        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql.append("AND LOWER(p.status) = LOWER(?) ");
            params.add(statusFilter);
        }
        if (stockFilter != null && !stockFilter.isEmpty()) {
            if (stockFilter.equals("In Stock")) {
                sql.append("AND EXISTS (SELECT 1 FROM ProductSize ps WHERE ps.pro_id = p.pro_id AND ps.stock > 0) ");
            } else if (stockFilter.equals("No Stock")) {
                sql.append("AND NOT EXISTS (SELECT 1 FROM ProductSize ps WHERE ps.pro_id = p.pro_id AND ps.stock > 0) ");
            }
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            sql.append("AND LOWER(p.pro_name) LIKE LOWER(?) ");
            params.add("%" + searchQuery + "%");
        }

        // Add sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy) {
                case "price_asc":
                    sql.append("ORDER BY p.price ASC ");
                    break;
                case "price_desc":
                    sql.append("ORDER BY p.price DESC ");
                    break;
                case "id_asc":
                    sql.append("ORDER BY p.pro_id ASC ");
                    break;
                case "id_desc":
                    sql.append("ORDER BY p.pro_id DESC ");
                    break;
                default:
                    sql.append("ORDER BY p.pro_id DESC ");
            }
        } else {
            sql.append("ORDER BY p.pro_id DESC ");
        }

        // Add pagination
        int offset = (page - 1) * itemsPerPage;
        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(itemsPerPage);

        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql.toString());
            
            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Product p = new Product();
                p.setPro_id(rs.getInt("pro_id"));
                p.setPro_name(rs.getString("pro_name"));
                p.setImage(rs.getString("image"));
                p.setGender(rs.getString("gender"));
                p.setBrand(rs.getString("brand"));
                
                Type t = new Type();
                t.setType_id(rs.getInt("type_id"));
                t.setType_name(rs.getString("type_name"));
                p.setType(t);
                
                p.setPrice(rs.getBigDecimal("price"));
                p.setDiscount(rs.getInt("discount"));
                p.setStatus(rs.getString("status"));
                
                // Get product sizes for this product
                List<ProductSize> productSizes = getProductSizes(p.getPro_id());
                p.setProductSizes(productSizes);
                
                products.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        
        return products;
    }

    private List<ProductSize> getProductSizes(int productId) {
        List<ProductSize> sizes = new ArrayList<>();
        String sql = "SELECT size_id, pro_id, size, stock FROM ProductSize WHERE pro_id = ?";
        
        Connection sizeConn = null;
        PreparedStatement sizePs = null;
        ResultSet sizeRs = null;
        
        try {
            sizeConn = new DBcontext().getConnection();
            sizePs = sizeConn.prepareStatement(sql);
            sizePs.setInt(1, productId);
            sizeRs = sizePs.executeQuery();
            
            while (sizeRs.next()) {
                ProductSize ps = new ProductSize();
                ps.setSize_id(sizeRs.getInt("size_id"));
                ps.setPro_id(sizeRs.getInt("pro_id"));
                ps.setSize(sizeRs.getString("size"));
                ps.setStock(sizeRs.getInt("stock"));
                sizes.add(ps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sizeRs != null) sizeRs.close();
                if (sizePs != null) sizePs.close();
                if (sizeConn != null) sizeConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return sizes;
    }

    public int getTotalFilteredProducts(String typeFilter, String genderFilter, String brandFilter,
            String statusFilter, String stockFilter, String searchQuery) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM Product p "
                + "JOIN Type t ON p.type_id = t.type_id "
                + "WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();

        // Add filter conditions
        if (genderFilter != null && !genderFilter.isEmpty()) {
            sql.append("AND LOWER(p.gender) = LOWER(?) ");
            params.add(genderFilter);
        }
        if (typeFilter != null && !typeFilter.isEmpty()) {
            sql.append("AND LOWER(t.type_name) = LOWER(?) ");
            params.add(typeFilter);
        }
        if (brandFilter != null && !brandFilter.isEmpty()) {
            sql.append("AND LOWER(p.brand) = LOWER(?) ");
            params.add(brandFilter);
        }
        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql.append("AND LOWER(p.status) = LOWER(?) ");
            params.add(statusFilter);
        }
        if (stockFilter != null && !stockFilter.isEmpty()) {
            if (stockFilter.equals("In Stock")) {
                sql.append("AND EXISTS (SELECT 1 FROM ProductSize ps WHERE ps.pro_id = p.pro_id AND ps.stock > 0) ");
            } else if (stockFilter.equals("No Stock")) {
                sql.append("AND NOT EXISTS (SELECT 1 FROM ProductSize ps WHERE ps.pro_id = p.pro_id AND ps.stock > 0) ");
            }
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            sql.append("AND LOWER(p.pro_name) LIKE LOWER(?) ");
            params.add("%" + searchQuery + "%");
        }

        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean addProduct(Product product) {
        Connection conn = null;
        PreparedStatement psProduct = null;
        PreparedStatement psProductSize = null;
        ResultSet generatedKeys = null;
        boolean success = false;

        try {
            conn = new DBcontext().getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Insert into Product table
            String sqlProduct = "INSERT INTO Product (pro_name, image, gender, brand, type_id, price, discount, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            psProduct = conn.prepareStatement(sqlProduct, PreparedStatement.RETURN_GENERATED_KEYS);
            psProduct.setString(1, product.getPro_name());
            psProduct.setString(2, product.getImage());
            psProduct.setString(3, product.getGender());
            psProduct.setString(4, product.getBrand());
            psProduct.setInt(5, product.getType().getType_id());
            psProduct.setBigDecimal(6, product.getPrice());
            psProduct.setInt(7, product.getDiscount());
            psProduct.setString(8, product.getStatus());

            int rowsAffected = psProduct.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating product failed, no rows affected.");
            }

            // Get the generated product ID
            generatedKeys = psProduct.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Creating product failed, no ID obtained.");
            }
            int productId = generatedKeys.getInt(1);

            // Insert into ProductSize table
            String sqlProductSize = "INSERT INTO ProductSize (pro_id, size, stock) VALUES (?, ?, ?)";
            psProductSize = conn.prepareStatement(sqlProductSize);

            // Insert product sizes
            for (ProductSize productSize : product.getProductSizes()) {
                psProductSize.setInt(1, productId);
                psProductSize.setString(2, productSize.getSize());
                psProductSize.setInt(3, productSize.getStock());
                psProductSize.executeUpdate();
            }

            conn.commit(); // Commit transaction
            success = true;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (psProductSize != null) psProductSize.close();
                if (psProduct != null) psProduct.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    public boolean updateProduct(Product product) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            conn = new DBcontext().getConnection();
            conn.setAutoCommit(false);  // Start transaction

            // Update product information
            String updateProductSql = "UPDATE Product "
                    + "SET pro_name=?, image=?, gender=?, brand=?, type_id=?, "
                    + "price=?, discount=?, status=? "
                    + "WHERE pro_id=?";
            
            ps = conn.prepareStatement(updateProductSql);
            ps.setString(1, product.getPro_name());
            ps.setString(2, product.getImage());
            ps.setString(3, product.getGender());
            ps.setString(4, product.getBrand());
            ps.setInt(5, product.getType().getType_id());
            ps.setBigDecimal(6, product.getPrice());
            ps.setInt(7, product.getDiscount());
            ps.setString(8, product.getStatus());
            ps.setInt(9, product.getPro_id());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Updating product failed, no rows affected.");
            }

            // Update stock for each existing size
            String updateStockSql = "UPDATE ProductSize SET stock = ? WHERE pro_id = ? AND size = ?";
            ps = conn.prepareStatement(updateStockSql);
            
            // Update stock for each size
            for (ProductSize productSize : product.getProductSizes()) {
                ps.setInt(1, productSize.getStock());
                ps.setInt(2, product.getPro_id());
                ps.setString(3, productSize.getSize());
                ps.executeUpdate();
            }

            conn.commit();
            success = true;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



