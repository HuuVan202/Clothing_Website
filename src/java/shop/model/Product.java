/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
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
    private int type_id;
    private String status;
    private int discount;
    private BigDecimal discountedPrice, salePrice;
    private Type type;
    private List<ProductSize> productSizes;

    private static final NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
    private Double averageRating;
    private int feedbackCount;
    private int soldProduct; 
    private String[] stocks;

    public Product() {
    }

    public Product(int pro_id, String pro_name, BigDecimal price, String image, String gender, String brand, String status, int discount) {
        this.pro_id = pro_id;
        this.pro_name = pro_name;
        this.price = price;
        this.image = image;
        this.gender = gender;
        this.brand = brand;
        this.status = status;
        this.discount = discount;
    }

    public Product(int pro_id, String pro_name, BigDecimal price, int stock, String image, String size, String gender, String brand, String status, int discount) {
        this.pro_id = pro_id;
        this.pro_name = pro_name;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.size = size;
        this.gender = gender;
        this.brand = brand;
        this.status = status;
        this.discount = discount;
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
        this.type_id = cat_id;
        this.status = status;
        this.discount = discount;
        this.discountedPrice = discountedPrice;
    }

    public Product(String pro_name, String image) {
        this.pro_name = pro_name;
        this.image = image;
    }

    public Product(int pro_id, String pro_name, BigDecimal price, int stock,
            String image, String size, String gender, String brand, int type_id,
            String status, int discount) {
        this.pro_id = pro_id;
        this.pro_name = pro_name;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.size = size;
        this.gender = gender;
        this.brand = brand;
        this.type_id = type_id;
        this.status = status;
        this.discount = discount;
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

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
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

    public BigDecimal getSalePrice() {
        if (price != null && discount > 0) {
            BigDecimal discountAmount = price.multiply(BigDecimal.valueOf(discount)).divide(BigDecimal.valueOf(100));
            salePrice = price.subtract(discountAmount);
        } else {
            salePrice = price;
        }
        return salePrice;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    // Getter và Setter cho averageRating
    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getFeedbackCount() {
        return feedbackCount;
    }

    public void setFeedbackCount(int feedbackCount) {
        this.feedbackCount = feedbackCount;
    }

    public int getSoldProduct() {
        return soldProduct;
    }

    public void setSoldProduct(int soldProduct) {
        this.soldProduct = soldProduct;
    }

    public String[] getStocks() {
        return stocks;
    }

    public void setStocks(String[] stocks) {
        this.stocks = stocks;
    }

    public List<ProductSize> getProductSizes() {
        return productSizes;
    }

    public void setProductSizes(List<ProductSize> productSizes) {
        this.productSizes = productSizes;
    }

    @Override
    public String toString() {
        return "Product{" + "pro_id=" + pro_id + ", pro_name=" + pro_name + ", price=" + price
                + ", image=" + image + ", gender=" + gender + ", brand=" + brand
                + ", type_id=" + type_id + ", status=" + status + ", discount=" + discount
                + ", productSizes=" + (productSizes != null ? productSizes : "[]") + '}';
    }
}
