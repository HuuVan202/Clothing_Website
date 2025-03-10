/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.customer.orders;

import java.sql.SQLException;
import shop.context.DBcontext;

/**
 *
 * @author Admin
 */
public class CancelDAO extends DBcontext {

    public boolean cancelOrder(int id) {
        boolean isSuccess = false;
        connection = getConnection();

        try {
            String sql = """
                         UPDATE [dbo].[Order]
                            SET [tracking] = 'cancelled'
                          WHERE [order_id] = ?""";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();

            isSuccess = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return isSuccess;
    }

    public static void main(String[] args) {
        CancelDAO dao = new CancelDAO();
        if (dao.cancelOrder(2)) {
            System.out.println("oke");
        } else {
            System.out.println("no");
        }
    }
}
