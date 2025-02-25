/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.admin.accountManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import shop.context.DBcontext;
import shop.model.Account;
import shop.model.Customer;

/**
 *
 * @author ADMIN
 */
public class accountDAO {
         private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

   public List<Object[]> getAllCustomerAccounts() {
    List<Object[]> customers = new ArrayList<>();
    String sql = "SELECT c.cus_id, c.cus_name, a.username, a.password, c.email, c.phone, c.address, " +
                     "a.role, a.acc_status " +
                     "FROM Customer c " +
                     "JOIN Account a ON c.username = a.username " +
                     "WHERE a.role = 'customer'";

    try {
        connection = new DBcontext().getConnection(); // Kết nối database
        statement = connection.prepareStatement(sql); 
        resultSet = statement.executeQuery(); 

        while (resultSet.next()) {
            Customer customer = new Customer(
                        resultSet.getInt("cus_id"),
                        resultSet.getString("cus_name"),
                        resultSet.getString("email"),
                        resultSet.getString("username"),
                        resultSet.getString("phone"),
                        resultSet.getString("address")
                );

             Account account = new Account(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("role"),
                        resultSet.getString("acc_status")
                );

            customers.add(new Object[]{customer, account});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Đóng tài nguyên sau khi sử dụng
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return customers;
}

    
     public void updateAccount(Account accountUpdate) {
        String sql = "UPDATE [dbo].[Account] " +
                     "SET [password] = ?, " +
                     "    [role] = ?, " +
                     "    [acc_status] = ? " +
                     "WHERE [username] = ?";

        try {
            connection = new DBcontext().getConnection(); // Kết nối database
            statement = connection.prepareStatement(sql);

            // Gán giá trị cho các tham số trong câu lệnh SQL
            statement.setString(1, accountUpdate.getPassword());
            statement.setString(2, accountUpdate.getRole());
            statement.setString(3, accountUpdate.getAcc_status());
            statement.setString(4, accountUpdate.getUsername());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cập nhật tài khoản thành công!");
            } else {
                System.out.println("Không tìm thấy tài khoản cần cập nhật.");
            }
        } catch (SQLException e) {
        e.printStackTrace(); }
    }
     // Lấy tài khoản theo username
    public Account getAccountByUsername(String username) {
        String sql = "SELECT username, password, role, acc_status FROM Account WHERE username = ?";
        try {
            connection = new DBcontext().getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Account(
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("role"),
                    resultSet.getString("acc_status")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return null;
    }


    

   public static void main(String[] args) {
       accountDAO dao = new accountDAO();
       List<Object[]> customerList = dao.getAllCustomerAccounts();
       for (Object[] obj : customerList) {
           Customer customer = (Customer) obj[0];
           Account account = (Account) obj[1];
           System.out.println(customer + " | " + account);
       }
    }
}
