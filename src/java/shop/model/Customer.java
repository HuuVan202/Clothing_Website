/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

/**
 *
 * @author
 */
public class Customer {

    private int cus_id;
    private String cus_name;
    private String email;
    private String username;
    private String phone;
    private String address;

    public Customer(int cus_id, String cus_name, String email, String username, String phone, String address) {
        this.cus_id = cus_id;
        this.cus_name = cus_name;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.address = address;
    }

    public Customer(String cus_name) {
        this.cus_name = cus_name;
    }

    public Customer(String cus_name, String email, String username, String phone, String address) {
        this.cus_name = cus_name;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.address = address;
    }

    public int getCus_id() {
        return cus_id;
    }

    public void setCus_id(int cus_id) {
        this.cus_id = cus_id;
    }

    public String getCus_name() {
        return cus_name;
    }

    public void setCus_name(String cus_name) {
        this.cus_name = cus_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Customer{" + "cus_id=" + cus_id + ", cus_name=" + cus_name + ", email=" + email + ", username=" + username + ", phone=" + phone + ", address=" + address + '}';
    }

}
