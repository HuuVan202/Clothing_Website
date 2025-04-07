/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

/**
 *
 * @author
 */
public class Account {

    private int accountID;
    private String username;
    private String password;
    private String role;
    private String acc_status;

    public Account(int accountID, String username, String password, String role, String acc_status) {
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.role = role;
        this.acc_status = acc_status;
    }

    public Account(String username, String password, String role, String acc_status) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.acc_status = acc_status;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAcc_status() {
        return acc_status;
    }

    public void setAcc_status(String acc_status) {
        this.acc_status = acc_status;
    }

    @Override
    public String toString() {
        return "Account{" + "username=" + username + ", password=" + password + ", role=" + role + ", acc_status=" + acc_status + '}';
    }

}
