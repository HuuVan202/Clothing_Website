/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.guest.products;

/**
 *
 * @author Admin
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import shop.context.DBcontext;
import shop.model.Product;

public class productDAO extends DBcontext {

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
                        resultSet.getInt("stock"),
                        resultSet.getString("image"),
                        resultSet.getString("size"),
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

    public static void main(String[] args) {
        productDAO dao = new productDAO();
        int id = 1;

        Product product = dao.getProductById(id);
        if (product != null) {
            System.out.println(product);
        } else {
            System.out.println("Không tìm thấy sản phẩm với ID: " + id);
        }
    }

}
