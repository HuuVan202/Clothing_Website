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
        String sql = "SELECT c.pro_id, c.size, c.quantity, ps.stock "
                + "FROM cart c "
                + "JOIN productSize ps ON c.pro_id = ps.pro_id AND c.size = ps.size "
                + "WHERE c.cus_id = ?";

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);
            try (ResultSet rs = statement.executeQuery()) {
                ProductDAO dao = new ProductDAO();
                while (rs.next()) {
                    int productId = rs.getInt("pro_id");
                    String size = rs.getString("size");
                    int quantity = rs.getInt("quantity");
                    int stock = rs.getInt("stock");

                    Product product = dao.getProductById(productId);
                    if (product != null) {
                        CartItem item = new CartItem(product, quantity, size);
                        item.setStock(stock);
                        items.add(item);
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


    public static void addItem(int customerId, CartItem cartItem) {
        DBcontext db = new DBcontext();

        String sql = "INSERT INTO cart (cus_id, pro_id, size, quantity) VALUES (?, ?, ?, ?)";

        try (Connection connection = db.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);
            statement.setInt(2, cartItem.getProduct().getPro_id());
            statement.setString(3, cartItem.getSize());
            statement.setInt(4, cartItem.getQuantity());
            statement.executeUpdate();

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
        String sql = "INSERT INTO cart (cus_id, pro_id, size, quantity) VALUES (?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE quantity = ?";

        try (Connection connection = db.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.setInt(2, item.getProduct().getPro_id());
            statement.setString(3, item.getSize());
            statement.setInt(4, item.getQuantity());
            statement.setInt(5, item.getQuantity());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeCartItem(int customerId, int productId, String size) {
        DBcontext db = new DBcontext();
        String sql = "DELETE FROM cart WHERE cus_id = ? AND pro_id = ? AND size = ?";

        try (Connection connection = db.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.setInt(2, productId);
            statement.setString(3, size);
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

    public static boolean updateCartItem(int customerId, int productId, String size, int quantity) {
        DBcontext db = new DBcontext();
        String sql = "UPDATE cart SET quantity = ? WHERE cus_id = ? AND pro_id = ? AND size = ?";
        try (Connection connection = db.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, quantity);
            statement.setInt(2, customerId);
            statement.setInt(3, productId);
            statement.setString(4, size);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
                        + item.getQuantity() + item.getSize());
            }
        }
    }
}
