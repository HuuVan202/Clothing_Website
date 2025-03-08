/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author
 */
public class Order {

    private int order_id;
    private int cus_id;
    private BigDecimal total_price;
    private String tracking;
    private Date order_date;
    private String payment_method;
    private Customer customer;

    private static final NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

    public Order() {
    }

    public Order(int order_id, int cus_id, BigDecimal total_price, String tracking, Date order_date, String payment_method) {
        this.order_id = order_id;
        this.cus_id = cus_id;
        this.total_price = total_price;
        this.tracking = tracking;
        this.order_date = order_date;
        this.payment_method = payment_method;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getCus_id() {
        return cus_id;
    }

    public void setCus_id(int cus_id) {
        this.cus_id = cus_id;
    }

    public BigDecimal getTotal_price() {
        return total_price;
    }

    public void setTotal_price(BigDecimal total_price) {
        this.total_price = total_price;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getFormattedPrice() {
        return formatter.format(total_price);
    }

    @Override
    public String toString() {
        return "Order{" + "order_id=" + order_id + ", cus_id=" + cus_id + ", total_price=" + total_price + ", tracking=" + tracking + ", order_date=" + order_date + ", payment_method=" + payment_method + ", customer=" + customer + '}';
    }

}
