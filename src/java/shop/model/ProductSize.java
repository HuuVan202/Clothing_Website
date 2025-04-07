/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

/**
 *
 * @author Quan
 */
public class ProductSize {

    private int size_id;
    private int pro_id;
    private String size;
    private int stock;

    public ProductSize(int size_id, int pro_id, String size, int stock) {
        this.size_id = size_id;
        this.pro_id = pro_id;
        this.size = size;
        this.stock = stock;
    }

    public ProductSize( int pro_id, String size, int stock) {
        this.pro_id = pro_id;
        this.size = size;
        this.stock = stock;
    }

    public ProductSize(String size, int stock) {

        this.size = size;
        this.stock = stock;
    }

    public ProductSize() {
    }

    public int getSize_id() {
        return size_id;
    }

    public void setSize_id(int size_id) {
        this.size_id = size_id;
    }

    public int getPro_id() {
        return pro_id;
    }

    public void setPro_id(int pro_id) {
        this.pro_id = pro_id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ProductSize{" + "size_id=" + size_id + ", pro_id=" + pro_id + ", size=" + size + ", stock=" + stock + '}';
    }

}
