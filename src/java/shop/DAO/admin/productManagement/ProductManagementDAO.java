package shop.DAO.admin.productManagement;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import shop.context.DBcontext;
import shop.model.Product;
import shop.model.Type;

public class ProductManagementDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public List<Product> getProductsByPage(int start, int limit) {
        List<Product> productList = new ArrayList<>();
        if (start < 0) {
            start = 0; // ??m b?o OFFSET không b? âm
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
        return 10; // Hi?n th? t?i ?a 10 trang
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
        return -1; // Tr? v? -1 n?u không tìm th?y
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
            return rowsAffected > 0; // Ch? tr? v? true n?u có dòng ???c chèn

        } catch (Exception e) {
            e.printStackTrace();
            return false; // N?u có l?i, tr? v? false
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
                if (rs != null) {
                    rs.close();
                }
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
        return null;
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE Product SET pro_name=?, image=?, size=?, gender=?, brand=?, type_id=?, price=?, discount=?, stock=?, status=? WHERE pro_id=?";
        try {
            conn = new DBcontext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getPro_name());
            ps.setString(2, product.getImage());
            
            // Handle size based on type_id
            int typeId = product.getType().getType_id();
            String size;
            if (typeId >= 6 && typeId <= 9) {
                size = "One Size";
            } else {
                size = product.getSize(); // Keep original size for clothing items
            }
            ps.setString(3, size);
            
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

    public List<Product> getFilteredAndSortedProducts(String typeFilter, String genderFilter, String brandFilter,
            String statusFilter, String stockFilter, String searchQuery, String sortBy, int page, int itemsPerPage) {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.pro_id, p.pro_name, p.image, COALESCE(p.size, '') AS size, "
                + "p.gender, p.brand, p.type_id, t.type_name, p.price, p.discount, "
                + "p.stock, p.status FROM Product p "
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
                sql.append("AND p.stock > 0 ");
            } else if (stockFilter.equals("No Stock")) {
                sql.append("AND p.stock = 0 ");
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
        sql.append("OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add((page - 1) * itemsPerPage);
        params.add(itemsPerPage);

        try (Connection conn = new DBcontext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setPro_id(rs.getInt("pro_id"));
                product.setPro_name(rs.getString("pro_name"));
                product.setImage(rs.getString("image"));
                product.setSize(rs.getString("size"));
                product.setGender(rs.getString("gender"));
                product.setBrand(rs.getString("brand"));

                Type type = new Type();
                type.setType_id(rs.getInt("type_id"));
                type.setType_name(rs.getString("type_name"));
                product.setType(type);

                product.setPrice(rs.getBigDecimal("price"));
                product.setDiscount(rs.getInt("discount"));
                product.setStock(rs.getInt("stock"));
                product.setStatus(rs.getString("status"));

                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
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
                sql.append("AND p.stock > 0 ");
            } else if (stockFilter.equals("No Stock")) {
                sql.append("AND p.stock = 0 ");
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

    public static void main(String[] args) {
        ProductManagementDAO p = new ProductManagementDAO();
        p.getAllTypes();
    }
}
