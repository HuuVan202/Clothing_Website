package shop.model;

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

    public void removeItemToCart(int productId, String size) {
        CartDAO.removeCartItem(customerId, productId, size);
        items.removeIf(i -> i.getProduct().getPro_id() == productId && i.getSize().equals(size));
    }

    public void updateItemToCart(int productId, String size, int quantity) {
        boolean updateSuccessful = CartDAO.updateCartItem(customerId, productId, size, quantity);

        if (updateSuccessful) {
            for (CartItem item : items) {
                if (item.getProduct().getPro_id() == productId && item.getSize().equals(size)) {
                    item.setQuantity(quantity);
                    break;
                }
            }
        } else {
            System.out.println("Update cart item failed");
        }
    }

    public int getItemQuantity(int productId, String size) {
        for (CartItem item : items) {
            if (item.getProduct().getPro_id() == productId && item.getSize().equals(size)) {
                return item.getQuantity();
            }
        }
        return 0;
    }

    public List<CartItem> getItems() {
        return items;
    }
}


