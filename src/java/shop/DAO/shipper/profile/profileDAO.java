/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.shipper.profile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import shop.context.DBcontext;
import shop.model.Customer;

/**
 *
 * @author Admin
 */
public class profileDAO {

    public static List<Customer> getShipper() {
        List<Customer> customers = new ArrayList<>();
        String sql = " SELECT *\n"
                + "FROM \n"
                + "   [dbo].[Customer] c\n"
                + "JOIN \n"
                + "    [ClothingShopDB].[dbo].[Account] a ON c.[username] = a.[username]\n"
                + "WHERE \n"
                + "    a.[role] = 'shipper'	";

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {

                    Customer customer = new Customer();
                    customer.setCus_id(rs.getInt("cus_id"));
                    customer.setCus_name(rs.getString("cus_name"));
                    customer.setEmail(rs.getString("email"));
                    customer.setUsername(rs.getString("username"));
                    customer.setPhone(rs.getString("phone"));
                    customer.setAddress(rs.getString("address"));
                    customers.add(customer);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public boolean updateShipperProfile(String fullName, String phone, String address, String username) {
        // Input validation
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Username cannot be null or empty");
            return false;
        }

        String sql = """
               UPDATE c
                SET c.[cus_name] = ?,
                    c.[phone] = ?,
                    c.[address] = ?
                FROM [dbo].[Customer] c
                INNER JOIN [dbo].[Account] a ON c.username = a.username
                WHERE c.[username] = ?
            """;

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set parameters
            statement.setString(1, fullName != null ? fullName.trim() : "");
            statement.setString(2, phone != null ? phone.trim() : "");
            statement.setString(3, address != null ? address.trim() : "");
            statement.setString(4, username.trim());

            int rowsUpdated = statement.executeUpdate();

            // Only return true if exactly one row was updated
            return rowsUpdated == 1;

        } catch (SQLException e) {
            System.err.println("Error updating shipper profile: " + e.getMessage());
            return false;
        }
    }

    public Customer getCustomer(String userName) {
        Customer customer = null;

        String sql = """
                 SELECT [cus_id], [cus_name], [email], [username], [phone], [address]
                 FROM [ClothingShopDB].[dbo].[Customer]
                 WHERE [username] = ?
                 """;
        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userName.trim());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("cus_id");
                    String name = rs.getString("cus_name");
                    String email = rs.getString("email");
                    String username = rs.getString("username");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    customer = new Customer(id, name, email, username, phone, address);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting customer: " + e.getMessage());
        }

        return customer;
    }

    public static void main(String[] args) {
        profileDAO dao = new profileDAO();

        // Test case - successful update
        boolean success1 = dao.updateShipperProfile(
                "Nguyen Van A",
                "0912345678",
                "123 Delivery Street, Hanoi",
                "quannhce181867@fpt.edu.vn"
        );
        System.out.println("Update 1: " + (success1 ? "Success" : "Failed"));

    }
}
