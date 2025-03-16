/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.guest.forgotPassword;

import java.sql.SQLException;
import shop.context.DBcontext;

/**
 *
 * @author Admin
 */
public class ForgotPasswordDAO extends DBcontext {

    public String getUsernameByMail(String mail) {
        String userName = "";
        connection = getConnection();

        try {
            String sql = """
                         SELECT [username]
                           FROM [ClothingShopDB].[dbo].[Customer]
                           WHERE [email] = ?""";
            
            statement = connection.prepareStatement(sql);
            statement.setString(1, mail);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {                
                userName = resultSet.getString(1);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userName;
    }
    
    public static void main(String[] args) {
        ForgotPasswordDAO dao = new ForgotPasswordDAO();
        System.out.println(dao.getUsernameByMail("a@example.com"));
    }
}
