/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.guest.login;

import java.sql.SQLException;
import shop.DAO.guest.singup.HashPassword;
import shop.context.DBcontext;
import shop.model.Account;
import shop.model.Customer;

/**
 *
 * @author Admin
 */
public class LoginDAO extends DBcontext {

    public Account checkLoginAccount(String userName, String passWord) {
        Account account = null;
        connection = getConnection();
        HashPassword hash = new HashPassword();

        try {
            String sql = """
                        SELECT [accountID]
                              ,[username]
                              ,[password]
                              ,[role]
                              ,[acc_status]
                          FROM [ClothingShopDB].[dbo].[Account]
                         WHERE [username] = ? AND [password] = ?""";

            String passHashed = hash.hashPassword(passWord);
            statement = connection.prepareStatement(sql);
            statement.setString(1, userName.trim());
            statement.setString(2, passHashed);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int accountID = resultSet.getInt(1);
                String userNameFinded = resultSet.getString(2);
                String passwordFinded = resultSet.getString(3);
                String role = resultSet.getString(4);
                String status = resultSet.getString(5);

                account = new Account(accountID, userNameFinded, passwordFinded, role, status);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return account;
    }

    public Customer getCustomer(String userName) {
        Customer customer = null;
        connection = getConnection();

        try {
            String sql = """
                         SELECT TOP (1000) [cus_id]
                               ,[cus_name]
                               ,[email]
                               ,[username]
                               ,[phone]
                               ,[address]
                           FROM [ClothingShopDB].[dbo].[Customer]
                         WHERE [username] = ?""";

            statement = connection.prepareCall(sql);
            statement.setString(1, userName.trim());

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                String username = resultSet.getString(4);
                String phone = resultSet.getString(5);
                String address = resultSet.getString(6);
                customer = new Customer(id, name, email, username, phone, address);
            }

        } catch (SQLException e) {
            System.out.println(e);
        }

        return customer;
    }

    public Customer getCustomerByEmail(String emailInput) {
        Customer customer = null;
        connection = getConnection();

        try {
            String sql = """
                         SELECT [cus_id]
                               ,[cus_name]
                               ,[email]
                               ,[username]
                               ,[phone]
                               ,[address]
                           FROM [dbo].[Customer]
                           WHERE [email] = ?""";

            statement = connection.prepareStatement(sql);
            statement.setString(1, emailInput);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                String username = resultSet.getString(4);
                String phone = resultSet.getString(5);
                String address = resultSet.getString(6);
                customer = new Customer(id, name, email, username, phone, address);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customer;
    }

    public static void main(String[] args) {
        LoginDAO loginDAO = new LoginDAO();
        Account c = loginDAO.checkLoginAccount("customer10", "Huuvan@2004");
//        Customer c = loginDAO.getCustomer("huuvan2004");
//        Customer c = loginDAO.getCustomerByEmail("donatellophan@gmail.com");

        System.out.println(c);
        if (c != null) {
            System.out.println("Đăng nhập thành công!");
        } else {
            System.out.println("Sai tài khoản hoặc mật khẩu.");
        }
    }
}
