/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author
 */
public class Product {

    private int pro_id;
    private String pro_name;
    private BigDecimal price;
    private int stock;
    private String image;
    private String size;
    private String gender;
    private String brand;
    private int cat_id;
    private String status;
    private int discount;
    private BigDecimal discountedPrice;

    private static final NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));

    public Product() {
    }

    public Product(String pro_name, String image) {
        this.pro_name = pro_name;
        this.image = image;
    }


    public Product(int pro_id, String pro_name, BigDecimal price, int stock, String image, String size, String gender, String brand, int cat_id, String status, int discount, BigDecimal discountedPrice) {
        this.pro_id = pro_id;
        this.pro_name = pro_name;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.size = size;
        this.gender = gender;
        this.brand = brand;
        this.cat_id = cat_id;
        this.status = status;
        this.discount = discount;
        this.discountedPrice = discountedPrice;
    }

    public int getPro_id() {
        return pro_id;
    }

    public void setPro_id(int pro_id) {
        this.pro_id = pro_id;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getFormattedPrice() {
        return formatter.format(price);
    }

    public String getFormattedDiscountedPrice() {
        return formatter.format(discountedPrice);
    }

    @Override
    public String toString() {
        return "Product{" + "pro_id=" + pro_id + ", pro_name=" + pro_name + ", price=" + price + ", stock=" + stock + ", image=" + image + ", size=" + size + ", gender=" + gender + ", brand=" + brand + ", cat_id=" + cat_id + ", status=" + status + ", discount=" + discount + '}';
    }

}
