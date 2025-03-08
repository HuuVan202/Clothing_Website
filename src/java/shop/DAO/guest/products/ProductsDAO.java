package shop.DAO.guest.products;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import shop.context.DBcontext;
import shop.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductsDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Get all products without pagination
    public List<Product> getAllProductsUnpaginated() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT pro_id, pro_name, image, discount, price, gender, brand, type_id, "
                + "CASE WHEN discount > 0 THEN price * (1 - discount / 100.0) ELSE price END AS discounted_price "
                + "FROM Product WHERE status = 'active' "
                + "ORDER BY pro_id";
        try {
            conn = new DBcontext().getConnection();
            System.out.println("Executing SQL: " + sql);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            int count = 0;
            while (rs.next()) {
                Product p = new Product();
                p.setPro_id(rs.getInt("pro_id"));
                p.setPro_name(rs.getString("pro_name"));
                p.setImage(rs.getString("image"));
                p.setDiscount(rs.getInt("discount"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setDiscountedPrice(rs.getBigDecimal("discounted_price"));
                p.setGender(rs.getString("gender"));
                p.setBrand(rs.getString("brand"));
                p.setType_id(rs.getInt("type_id"));
                list.add(p);
                count++;
                System.out.println("Retrieved product - ID: " + p.getPro_id() + ", Name: " + p.getPro_name() + ", Type_ID: " + p.getType_id());
            }
            System.out.println("Total products retrieved: " + count);
        } catch (Exception e) {
            System.out.println("Error retrieving products: " + e.getMessage());
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
        return list;
    }

    // Show paginated products from a given list
    public List<Product> showProducts(List<Product> inputList, int index) {
        if (inputList == null || inputList.isEmpty()) {
            System.out.println("Input list is null or empty - Returning empty list");
            return new ArrayList<>();
        }
        int pageSize = 20; // 20 products per page
        int startIndex = (index - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, inputList.size());

        List<Product> paginatedList = new ArrayList<>();
        System.out.println("Paginating - Start Index: " + startIndex + ", End Index: " + endIndex + ", Total Products: " + inputList.size());
        for (int i = startIndex; i < endIndex; i++) {
            paginatedList.add(inputList.get(i));
            System.out.println("Added product to pagination - ID: " + inputList.get(i).getPro_id() + ", Name: " + inputList.get(i).getPro_name());
        }
        return paginatedList;
    }

    public List<Product> getProductsBySearch(List<Product> inputList, String search) {
        if (search == null || search.trim().isEmpty() || inputList == null || inputList.isEmpty()) {
            System.out.println("No search filter applied or input list empty - Returning original list with size: " + (inputList != null ? inputList.size() : 0));
            return inputList;
        }
        List<Product> filteredList = new ArrayList<>();
        String searchLower = search.trim().toLowerCase();
        System.out.println("Filtering by search: " + searchLower);
        for (Product p : inputList) {
            String proNameLower = p.getPro_name() != null ? p.getPro_name().toLowerCase() : "";
            if (proNameLower.contains(searchLower)) {
                filteredList.add(p);
            }
        }
        System.out.println("Found " + filteredList.size() + " products after search filter");
        return filteredList;
    }

    public List<Product> getProductsByGender(List<Product> inputList, String gender) {
        if (gender == null || gender.trim().isEmpty() || inputList == null || inputList.isEmpty()) {
            System.out.println("No gender filter applied or input list empty - Returning original list with size: " + (inputList != null ? inputList.size() : 0));
            return inputList;
        }
        List<Product> filteredList = new ArrayList<>();
        String genderLower = gender.trim().toLowerCase();
        System.out.println("Filtering by gender: " + genderLower);
        for (Product p : inputList) {
            if (p.getGender() != null && p.getGender().toLowerCase().equals(genderLower)) {
                filteredList.add(p);
            }
        }
        System.out.println("Found " + filteredList.size() + " products after gender filter");
        return filteredList;
    }

    public List<Product> getProductsByType(List<Product> inputList, String type) {
        if (type == null || type.trim().isEmpty() || inputList == null || inputList.isEmpty()) {
            System.out.println("No type filter applied or input list empty - Returning original list with size: " + (inputList != null ? inputList.size() : 0));
            return inputList;
        }
        List<Product> filteredList = new ArrayList<>();
        try {
            int typeId = Integer.parseInt(type.trim());
            System.out.println("Filtering by type_id: " + typeId);
            for (Product p : inputList) {
                System.out.println("Checking product type_id: " + p.getType_id() + ", Product Name: " + p.getPro_name() + ", Brand: " + p.getBrand());
                if (p.getType_id() == typeId) {
                    filteredList.add(p);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid type value: " + type + " - Returning original list");
            e.printStackTrace();
            return inputList;
        }
        System.out.println("Found " + filteredList.size() + " products after type filter");
        return filteredList;
    }

    public List<Product> getProductsByBrand(List<Product> inputList, String brand) {
        if (brand == null || brand.trim().isEmpty() || inputList == null || inputList.isEmpty()) {
            System.out.println("No brand filter applied or input list empty - Returning original list with size: " + (inputList != null ? inputList.size() : 0));
            return inputList;
        }
        List<Product> filteredList = new ArrayList<>();
        String brandLower = brand.trim().toLowerCase();
        System.out.println("Filtering by brand (lowercase): " + brandLower);
        for (Product p : inputList) {
            String productBrandLower = p.getBrand() != null ? p.getBrand().toLowerCase().trim() : "";
            System.out.println("Checking product brand (lowercase): " + productBrandLower + ", Product Name: " + p.getPro_name());
            if (productBrandLower.equals(brandLower)) {
                filteredList.add(p);
            }
        }
        System.out.println("Found " + filteredList.size() + " products after brand filter");
        return filteredList;
    }

    public List<Product> sortProducts(List<Product> inputList, String sort) {
        if (sort == null || sort.trim().isEmpty() || inputList == null || inputList.isEmpty()) {
            return inputList;
        }
        List<Product> sortedList = new ArrayList<>(inputList);
        switch (sort.trim()) {
            case "price_asc":
                sortedList.sort(Comparator.comparing(Product::getDiscountedPrice));
                break;
            case "price_desc":
                sortedList.sort(Comparator.comparing(Product::getDiscountedPrice, Comparator.reverseOrder()));
                break;
            case "newest":
                sortedList.sort(Comparator.comparing(Product::getPro_id, Comparator.reverseOrder()));
                break;
            case "oldest":
                sortedList.sort(Comparator.comparing(Product::getPro_id));
                break;
            default:
                break;
        }
        return sortedList;
    }

    public int getTotalFilteredProducts(String search, String gender, String type, String brand) {
        int total = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Product WHERE status = 'active'");
        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND LOWER(pro_name) LIKE ?");
            params.add("%" + search.trim().toLowerCase() + "%");
        }
        if (gender != null && !gender.trim().isEmpty()) {
            sql.append(" AND LOWER(gender) = ?");
            params.add(gender.trim().toLowerCase());
        }
        if (type != null && !type.trim().isEmpty()) {
            try {
                sql.append(" AND type_id = ?");
                params.add(Integer.parseInt(type.trim()));
                System.out.println("Adding type_id filter: " + Integer.parseInt(type.trim()));
            } catch (NumberFormatException e) {
                System.out.println("Invalid type value in total count: " + type + " - Returning 0");
                e.printStackTrace();
                return 0; // Return 0 if type is invalid
            }
        }
        if (brand != null && !brand.trim().isEmpty()) {
            sql.append(" AND LOWER(brand) = ?");
            params.add(brand.trim().toLowerCase());
        }

        try {
            conn = new DBcontext().getConnection();
            System.out.println("Executing total count SQL: " + sql.toString());
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
                System.out.println("Param " + (i + 1) + ": " + params.get(i));
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error counting filtered products: " + e.getMessage());
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
        System.out.println("Total products after filtering: " + total);
        return total;
    }
}
