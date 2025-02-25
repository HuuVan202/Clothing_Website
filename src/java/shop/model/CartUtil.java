/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class CartUtil {

    private List<CartItem> items;

    public CartUtil() {
        items = new ArrayList<>();
    }

    public CartUtil(List<CartItem> items) {
        this.items = items;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public CartItem getItemById(int pro_id) {
        for (CartItem item : items) {
            if (item.getProduct().getPro_id() == pro_id) {
                return item;
            }
        }
        return null;
    }

    public int getQuantityById(int pro_id) {
        CartItem item = getItemById(pro_id);
        return (item != null) ? item.getQuantity() : 0;
    }

    public void addItemToCart(CartItem item) {
        if (item == null || item.getProduct() == null) {
            throw new IllegalArgumentException("Item or Product cannot be null");
        }

        CartItem existingItem = getItemById(item.getProduct().getPro_id());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            items.add(item);
        }
    }

    public void updateQuantity(int pro_id, int quantity) {
        CartItem item = getItemById(pro_id);
        if (item != null) {
            item.setQuantity(quantity);
        }
    }

    public void removeItem(int pro_id) {
        CartItem item = getItemById(pro_id);
        if (item != null) {
            items.remove(item);
        }
    }

}
