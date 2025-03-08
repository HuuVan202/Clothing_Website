/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.customer.userProfile;

import java.sql.SQLException;
import shop.context.DBcontext;

/**
 *
 * @author HuuVan
 */
public class UserProfileDAO extends DBcontext {

    public boolean updateProfile(String fullName, String phone, String address, String username) {
        boolean isSuccess = false;
        connection = getConnection();

        try {
            String sql = """
                     UPDATE [dbo].[Customer]
                        SET [cus_name] = ?
                           ,[phone] = ?
                           ,[address] = ? 
                      WHERE [username] = ?""";

            statement = connection.prepareStatement(sql);
            statement.setString(1, fullName);
            statement.setString(2, phone);
            statement.setString(3, address);
            statement.setString(4, username);

            statement.executeUpdate();

            isSuccess = true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return isSuccess;
    }

    public static void main(String[] args) {
        UserProfileDAO dao = new UserProfileDAO();

        if (dao.updateProfile("aaaa", "123131", "Can Tho", "customer1")) {
            System.out.println("Thanh cong");
        } else {
            System.out.println("khong thanh cong");
        }

    }

}
