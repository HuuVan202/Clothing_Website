/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import shop.DAO.customer.cart.CartDAO;

/**
 *
 * @author Admin
 */
public class CartUtil {

    private List<CartItem> items;
    private int customerId;

    public CartUtil() {
        items = new ArrayList<>();

    }

    public CartUtil(int customerId) {
        this.customerId = customerId;
        this.items = new ArrayList<>();
        showCart();
    }

    public void showCart() {
        this.items = CartDAO.getCartItemsByCustomerId(customerId);
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void addItemToCart(CartItem item) {
        CartDAO.addCartItem(customerId, item);
        items.add(item);

    }

    public void removeItemToCart(int productId) {
        CartDAO.removeCartItem(customerId, productId);
        items.removeIf(i -> i.getProduct().getPro_id() == productId);

    }

    public void updateItemToCart(int productId, int quantity) {
        CartDAO.updateCartItem(customerId, productId, quantity);
        for (CartItem item : items) {
            if (item.getProduct().getPro_id() == productId) {
                item.setQuantity(quantity);
                break;
            }
        }
    }

    public int getItemQuantity(int productId) {
        for (CartItem item : items) {
            if (item.getProduct().getPro_id() == productId) {
                return item.getQuantity();
            }
        }
        return 0;
    }

    public List<CartItem> getItems() {
        return items;
    }

}
