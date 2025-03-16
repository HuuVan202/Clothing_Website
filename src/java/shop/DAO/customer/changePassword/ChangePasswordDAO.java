/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.customer.changePassword;

import java.sql.SQLException;
import shop.DAO.guest.singup.HashPassword;
import shop.context.DBcontext;
import shop.model.Account;

/**
 *
 * @author Admin
 */
public class ChangePasswordDAO extends DBcontext {

    public boolean changePass(String username, String pass) {
        boolean isSuccess = false;
        HashPassword hashPass = new HashPassword();
        connection = getConnection();

        try {
            String sql = """
                     UPDATE [dbo].[Account]
                        SET [password] = ?
                      WHERE [username] = ?""";
            statement = connection.prepareStatement(sql);
            statement.setString(1, hashPass.hashPassword(pass));
            statement.setString(2, username);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                isSuccess = true;
                System.out.println("Password updated successfully for user: " + username);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return isSuccess;
    }

    public boolean checkCurrentPass(String userName, String currentPass) {
        boolean isSuccess = false;
        HashPassword hashPass = new HashPassword();
        connection = getConnection();

        try {
            String sql = """
                         SELECT [username]
                               ,[password]
                               ,[role]
                               ,[acc_status]
                           FROM [ClothingShopDB].[dbo].[Account]
                           WHERE [username] = ? AND [password] = ?""";
            statement = connection.prepareStatement(sql);
            statement.setString(1, userName);
            statement.setString(2, hashPass.hashPassword(currentPass));

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return isSuccess;
    }

    public static void main(String[] args) {
        ChangePasswordDAO dao = new ChangePasswordDAO();
        if (dao.checkCurrentPass("customer1", "Huuvan@2004")) {
            System.out.println("thanh cong");
        } else {
            System.out.println("khong");
        }
    }
}
