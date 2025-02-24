/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.admin.feedbackManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import shop.context.DBcontext;
import shop.model.Customer;
import shop.model.Feedback;
import shop.model.Product;

/**
 *
 * @author ADMIN
 */
public class feedbackManagementDAO {

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public List<Object[]> getAllFeedback() {
        List<Object[]> feedbackList = new ArrayList<>();

        String sql = "SELECT \n"
                + "    f.feedback_id,\n"
                + "  c.cus_name,\n"
                +"p.pro_name,\n"
                + "    p.image ,\n"
                + "    f.rating,\n"
                + "    f.comment,\n"
                + "    f.feedback_date\n"
                + "FROM Feedback f\n"
                + "JOIN Customer c ON f.cus_id = c.cus_id\n"
                + "JOIN Product p ON f.pro_id = p.pro_id;";

        try {
            connection = new DBcontext().getConnection(); // Kết nối database
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {

                /*  Feedback feedback = new Feedback(0,
                        resultSet.getInt("feedback_id"),
                        resultSet.getInt("pro_id"),
                        resultSet.getInt("cus_id"),
                        resultSet.getInt("rating"),
                        resultSet.getString("comment"),
                        resultSet.getDate("feedback_date"));
                feedbackList.add(feedback);*/
                Feedback feedback = new Feedback(
                        resultSet.getInt("feedback_id"),
                        resultSet.getInt("rating"),
                        resultSet.getString("comment"),
                        resultSet.getDate("feedback_date")
                );
                Customer customer = new Customer(
                        resultSet.getString("cus_name")
                );
                Product product = new Product(resultSet.getString("pro_name"),resultSet.getString("image"));
                feedbackList.add(new Object[]{feedback, customer, product});
            }
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý lỗi
        }
        return feedbackList;
    }
public static void main(String[] args) {
    feedbackManagementDAO dao = new feedbackManagementDAO();
    List<Object[]> feedbackList = dao.getAllFeedback();

    for (Object[] feedbackData : feedbackList) {
        Feedback feedback = (Feedback) feedbackData[0];
        Customer customer = (Customer) feedbackData[1];
        Product product = (Product) feedbackData[2];

        System.out.println("Feedback ID: " + feedback.getCus_id());
        System.out.println("Customer Name: " + customer.getCus_name());
        System.out.println("Product Image: " + product.getImage());
        System.out.println("Rating: " + feedback.getRating());
        System.out.println("Comment: " + feedback.getComment());
        System.out.println("Feedback Date: " + feedback.getFeedback_date());
        System.out.println("-----------------------------------");
    }
}
}
