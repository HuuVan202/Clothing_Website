/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.guest.singup;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import shop.context.DBcontext;
import shop.model.Account;
import shop.model.Customer;

/**
 *
 * @author HuuVan
 */
public class SignupDAO extends DBcontext {

    public boolean checkAccountExist(String username) {
        boolean isExist = false;
        connection = getConnection();

        try {
            String sql = """
                         SELECT [username]
                         FROM [dbo].[Account]
                         WHERE [username] = ?""";

            statement = connection.prepareStatement(sql);
            statement.setString(1, username.trim());
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                isExist = true;
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }

        return isExist;
    }

    public boolean checkEmailExist(String email) {
        boolean isExist = false;
        connection = getConnection();

        try {
            String sql = """
                         SELECT [email]
                           FROM [dbo].[Customer]
                           WHERE [email] = ?""";

            statement = connection.prepareStatement(sql);
            statement.setString(1, email.trim());
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                isExist = true;
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }

        return isExist;
    }

    public boolean signUp(Customer infor, Account account) {
        connection = getConnection();
        PreparedStatement statementAccount = null;
        PreparedStatement statementCustomer = null;
        HashPassword hash = new HashPassword();
        boolean isSuccess = false;

        try {
            connection.setAutoCommit(false);
            String sqlAccount = """
                                 INSERT INTO [dbo].[Account]
                                            ([username]
                                            ,[password]
                                            ,[role]
                                            ,[acc_status])
                                      VALUES
                                            (?
                                            ,?
                                            ,?
                                            ,?)""";
            String passHashed = hash.hashPassword(account.getPassword());

            statementAccount = connection.prepareStatement(sqlAccount);
            statementAccount.setString(1, account.getUsername().trim());
            statementAccount.setString(2, passHashed.trim());
            statementAccount.setString(3, account.getRole().trim());
            statementAccount.setString(4, account.getAcc_status().trim());
            statementAccount.executeUpdate();

            String sqlCustomer = """
                                 INSERT INTO [dbo].[Customer]
                                            ([cus_name]
                                            ,[email]
                                            ,[username]
                                            ,[phone]
                                            ,[address])
                                      VALUES
                                            (?
                                            ,?
                                            ,?
                                            ,?
                                            ,?)""";
            statementCustomer = connection.prepareStatement(sqlCustomer);
            statementCustomer.setString(1, infor.getCus_name().trim());
            statementCustomer.setString(2, infor.getEmail().trim());
            statementCustomer.setString(3, infor.getUsername().trim());
            statementCustomer.setString(4, infor.getPhone().trim());
            statementCustomer.setString(5, infor.getAddress().trim());
            statementCustomer.executeUpdate();

            connection.commit();
            isSuccess = true;

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println("Transaction rolled back due to error: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Đóng tài nguyên
            try {
                if (statementAccount != null) {
                    statementAccount.close();
                }
                if (statementCustomer != null) {
                    statementCustomer.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
        return isSuccess;
    }

    public static void main(String[] args) {
        SignupDAO dao = new SignupDAO();
        Account acc = new Account("hash2", "pass100", "customer", "active");
        Customer cus = new Customer("Nguyen Van Test1111111", "hash2@gmail.com", "hash2", "1231", "CanTho");

        if (dao.signUp(cus, acc)) {
            System.out.println("thanh cong");
        } else {
            System.out.println("That Bai");
        }

//        if (dao.checkEmailExist("a@example.com")) {
//            System.out.println("co");
//        } else {
//            System.out.println("khong");
//        }
    }
}
