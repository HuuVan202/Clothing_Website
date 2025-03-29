/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.customer.cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import shop.context.DBcontext;
import shop.model.Product;
import shop.model.ProductSize;

/**
 *
 * @author Admin
 */
public class ProductDAO extends DBcontext {

    public Product getProductById(int proId) {
        String sql = "SELECT * FROM Product WHERE pro_id = ?";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, proId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Product(
                        resultSet.getInt("pro_id"),
                        resultSet.getString("pro_name"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getString("image"),
                        resultSet.getString("gender"),
                        resultSet.getString("brand"),
                        resultSet.getString("status"),
                        resultSet.getInt("discount")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateStock(int productId, String size, int quantitySold) {
        String sql = "UPDATE productSize SET stock = stock - ? WHERE pro_id = ? AND size = ?";
        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantitySold);
            statement.setInt(2, productId);

            statement.setString(3, size);

            int rowsUpdated = statement.executeUpdate();

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getStockBySize(int productId, String size) {
        String sql = "SELECT stock FROM productSize WHERE pro_id = ? AND size = ?";
        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);
            statement.setString(2, size);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt("stock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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

    public int getAvailableStock(int productId, String sizeId) {
        DBcontext db = new DBcontext();

        String sql = "SELECT stock FROM ProductSize WHERE pro_id = ? AND size = ?";
        try (Connection connection = db.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId);
            statement.setString(2, sizeId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        ProductDAO dao = new ProductDAO();
        int productId = 10;

        String[] testSizes = {"S", "M", "L", "XL", "XXL", "INVALID_SIZE"};

        for (String size : testSizes) {
            int stock = dao.getStockBySize(productId, size);

            if (stock > 0) {
                System.out.println("Size " + size + ": Còn " + stock + " sản phẩm");
            } else if (stock == 0) {
                System.out.println("Size " + size + ": Hết hàng hoặc không tồn tại");
            }
        }

    }

}
