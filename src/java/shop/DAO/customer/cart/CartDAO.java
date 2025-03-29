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
import shop.model.CartItem;
import shop.model.CartUtil;
import shop.model.Product;

/**
 *
 * @author Admin
 */
public class CartDAO {

    public static List<CartItem> getCartItemsByCustomerId(int customerId) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT * FROM cart WHERE cus_id = ?";
        DBcontext db = new DBcontext();

        try (Connection connection = db.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("pro_id");
                    int quantity = rs.getInt("quantity");

                    ProductDAO dao = new ProductDAO();
                    Product product = dao.getProductById(productId);
                    if (product != null) {
                        items.add(new CartItem(product, quantity));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static CartUtil getCartByCustomerId(int customerId) {
        CartUtil cart = new CartUtil(customerId);
        cart.setItems(getCartItemsByCustomerId(customerId));
        return cart;
    }

<<<<<<< Updated upstream
<<<<<<< Updated upstream
    public static void saveCartToDatabase(int customerId, CartUtil cart) {
=======
    public static void addItem(int customerId, CartItem cartItem) {
>>>>>>> Stashed changes
=======
    public static void addItem(int customerId, CartItem cartItem) {
>>>>>>> Stashed changes
        DBcontext db = new DBcontext();

        String sql = "MERGE INTO cart AS target "
                + "USING (SELECT ? AS cus_id, ? AS pro_id, ? AS quantity) AS source "
                + "ON target.cus_id = source.cus_id AND target.pro_id = source.pro_id "
                + "WHEN MATCHED THEN UPDATE SET target.quantity = source.quantity "
                + "WHEN NOT MATCHED THEN INSERT (cus_id, pro_id, quantity) VALUES (source.cus_id, source.pro_id, source.quantity);";

        try (Connection connection = db.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            for (CartItem item : cart.getItems()) {
                statement.setInt(1, customerId);
                statement.setInt(2, item.getProduct().getPro_id());
                statement.setInt(3, item.getQuantity());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addToCart(int customerId, int productId, String size, int quantity) {
        DBcontext db = new DBcontext();

        String sql = "INSERT INTO cart_items (customer_id, product_id, size, quantity) VALUES (?, ?, ?, ?)";
        try (Connection connection = db.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.setInt(2, productId);
            statement.setString(3, size);
            statement.setInt(4, quantity);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addCartItem(int customerId, CartItem item) {
        DBcontext db = new DBcontext();
        String sql = "INSERT INTO cart (cus_id, pro_id, quantity) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE quantity = ?";

        try (Connection connection = db.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            System.out.println("Adding to DB: cus_id = " + customerId + ", pro_id = " + item.getProduct().getPro_id());

            statement.setInt(1, customerId);
            statement.setInt(2, item.getProduct().getPro_id());
            statement.setInt(3, item.getQuantity());
            statement.setInt(4, item.getQuantity());

            int rows = statement.executeUpdate();
            System.out.println("Rows affected: " + rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeCartItem(int customerId, int productId) {
        DBcontext db = new DBcontext();

        String sql = "DELETE FROM cart WHERE cus_id = ? AND pro_id = ?";

        try (Connection connection = db.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeAllCartItems(int customerId) {
        DBcontext db = new DBcontext();
        String sql = "DELETE FROM cart WHERE cus_id = ?";

        try (Connection connection = db.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCartItem(int customerId, int productId, int quantity) {
        DBcontext db = new DBcontext();

        String sql = "UPDATE cart SET quantity = ? WHERE cus_id = ? AND pro_id = ?";

        try (Connection connection = db.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, customerId);
            statement.setInt(3, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   
    public static void main(String[] args) {
        CartDAO dao = new CartDAO();
        int customerId = 1;

        List<CartItem> cartItems = dao.getCartItemsByCustomerId(customerId);

        if (cartItems.isEmpty()) {
            System.out.println(customerId);
        } else {
            for (CartItem item : cartItems) {
                System.out.println(item.getProduct().getPro_name()
                        + item.getQuantity());
            }
        }
    }
}
