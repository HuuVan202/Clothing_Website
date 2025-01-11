/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author
 */
public class Cart {

    private int cart_id;
    private int cus_id;
    private int pro_id;
    private int quantity;

    public Cart(int cart_id, int cus_id, int pro_id, int quantity) {
        this.cart_id = cart_id;
        this.cus_id = cus_id;
        this.pro_id = pro_id;
        this.quantity = quantity;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getCus_id() {
        return cus_id;
    }

    public void setCus_id(int cus_id) {
        this.cus_id = cus_id;
    }

    public int getPro_id() {
        return pro_id;
    }

    public void setPro_id(int pro_id) {
        this.pro_id = pro_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Cart{" + "cart_id=" + cart_id + ", cus_id=" + cus_id + ", pro_id=" + pro_id + ", quantity=" + quantity + '}';
    }

}
