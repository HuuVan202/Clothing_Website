/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;

/**
 *
 * @author
 */
public class OrderDetail {

    private int order_detail_id;
    private int order_id;
    private int pro_id;
    private int quantity;
    private BigDecimal price_per_unit;

    public OrderDetail(int order_detail_id, int order_id, int pro_id, int quantity, BigDecimal price_per_unit) {
        this.order_detail_id = order_detail_id;
        this.order_id = order_id;
        this.pro_id = pro_id;
        this.quantity = quantity;
        this.price_per_unit = price_per_unit;
    }

    public int getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(int order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
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

    public BigDecimal getPrice_per_unit() {
        return price_per_unit;
    }

    public void setPrice_per_unit(BigDecimal price_per_unit) {
        this.price_per_unit = price_per_unit;
    }

    @Override
    public String toString() {
        return "OrderDetail{" + "order_detail_id=" + order_detail_id + ", order_id=" + order_id + ", pro_id=" + pro_id + ", quantity=" + quantity + ", price_per_unit=" + price_per_unit + '}';
    }

}
