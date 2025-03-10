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
        String sql = "SELECT c.cus_id, c.cus_name, a.username, a.password, c.email, c.phone, c.address, "
                + "a.role, a.acc_status "
                + "FROM Customer c "
                + "JOIN Account a ON c.username = a.username "
                + "WHERE a.role = 'customer'";

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
        }
        return customers;
    }

    public boolean updateAccount(Account accountUpdate) {
        String sql = "UPDATE Account SET acc_status = ? WHERE username = ?";
        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, accountUpdate.getAcc_status());
            statement.setString(2, accountUpdate.getUsername());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Account updated successfully!");
                return true; // Trả về true nếu cập nhật thành công
            } else {
                System.out.println("Account not found.");
                return false; // Trả về false nếu không tìm thấy tài khoản
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }

    public void updateCustomer(Customer customerUpdate) {
    String sql = "UPDATE Customer SET cus_name = ?, phone = ?, address = ? WHERE cus_id = ?";
    try (Connection connection = new DBcontext().getConnection(); 
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, customerUpdate.getCus_name());
        statement.setString(2, customerUpdate.getPhone());
        statement.setString(3, customerUpdate.getAddress());
        statement.setInt(4, customerUpdate.getCus_id());

        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Customer updated successfully!");
        } else {
            System.out.println("Customer not found.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    // Lấy tài khoản theo username
    public Object[] getAccountById(int cus_Id) {
        String sql = "SELECT c.cus_id, c.cus_name, a.username, a.password, c.email, c.phone, c.address, "
                + "a.role, a.acc_status "
                + "FROM Customer c "
                + "JOIN Account a ON c.username = a.username "
                + "WHERE c.cus_id = ?";

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cus_Id);
              try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Object[]{
                        new Customer(
                        resultSet.getInt("cus_id"),
                        resultSet.getString("cus_name"),
                        resultSet.getString("email"),
                        resultSet.getString("username"),
                        resultSet.getString("phone"),
                        resultSet.getString("address")
                        ),
                        new Account(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("role"),
                        resultSet.getString("acc_status")
                        )};

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy tài khoản
    }

    public List<Object[]> searchCustomerAccountsByUsername(String username) {
        List<Object[]> customers = new ArrayList<>();
        String sql = "SELECT c.cus_id, c.cus_name, a.username, a.password, c.email, c.phone, c.address, "
                + "a.role, a.acc_status "
                + "FROM Customer c "
                + "JOIN Account a ON c.username = a.username "
                + "WHERE a.role = 'customer' AND a.username LIKE ?";

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + username + "%"); // Tìm username gần đúng
            ResultSet resultSet = statement.executeQuery();

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
        }
        return customers;
    }
    public List<Object[]> getFilteredCustomerAccounts(String username, String status) {
    List<Object[]> customers = new ArrayList<>();
    StringBuilder sql = new StringBuilder("SELECT c.cus_id, c.cus_name, a.username, a.password, c.email, c.phone, c.address, a.role, a.acc_status "
            + "FROM Customer c "
            + "JOIN Account a ON c.username = a.username WHERE 1=1");

    List<String> parameters = new ArrayList<>();

    if (status != null && !status.isEmpty() && !status.equals("all")) {
        sql.append(" AND a.acc_status = ?");
        parameters.add(status);
    }
    
    if (username != null && !username.isEmpty()) {
        sql.append(" AND a.username LIKE ?");
        parameters.add("%" + username + "%");
    }

    try (Connection connection = new DBcontext().getConnection();
         PreparedStatement statement = connection.prepareStatement(sql.toString())) {

        for (int i = 0; i < parameters.size(); i++) {
            statement.setString(i + 1, parameters.get(i));
        }

        ResultSet resultSet = statement.executeQuery();
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
    }
    return customers;
}

    


    public static void main(String[] args) {
        accountDAO dao = new accountDAO();
        int testCusId = 3; // Thay bằng ID hợp lệ trong database

        Object[] result = dao.getAccountById(testCusId);

        if (result != null) {
            Customer customer = (Customer) result[0];
            Account account = (Account) result[1];
            System.out.println("Thông tin Khách hàng:");
            System.out.println("ID: " + customer.getCus_id());
            System.out.println("Tên: " + customer.getCus_name());
            System.out.println("Email: " + customer.getEmail());
            System.out.println("Số điện thoại: " + customer.getPhone());
            System.out.println("Địa chỉ: " + customer.getAddress());

            System.out.println("\nThông tin Tài khoản:");
            System.out.println("Username: " + account.getUsername());
            System.out.println("Password: " + account.getPassword());
            System.out.println("Role: " + account.getRole());
            System.out.println("Trạng thái: " + account.getAcc_status());
        } else {
            System.out.println("Không tìm thấy tài khoản với ID: " + testCusId);
        }
    }

}
