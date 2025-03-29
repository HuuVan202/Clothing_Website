/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.admin.feedbackManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
                + "p.pro_name,\n"
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

                Feedback feedback = new Feedback(
                        resultSet.getInt("feedback_id"),
                        resultSet.getInt("rating"),
                        resultSet.getString("comment"),
                        resultSet.getDate("feedback_date")
                );
                Customer customer = new Customer(
                        resultSet.getString("cus_name")
                );
                Product product = new Product(
                        resultSet.getString("pro_name"),
                        resultSet.getString("image"));
                feedbackList.add(new Object[]{feedback, customer, product});
            }
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý lỗi
        }
        return feedbackList;
    }

    public List<Object[]> searchFeedbackByCustomername(String cus_name) {
        List<Object[]> feedbackList = new ArrayList<>();

        String sql = "SELECT f.feedback_id, c.cus_name, p.pro_name, p.image, f.rating, f.comment, f.feedback_date "
                + "FROM Feedback f "
                + "JOIN Customer c ON f.cus_id = c.cus_id "
                + "JOIN Product p ON f.pro_id = p.pro_id "
                + "WHERE c.cus_name LIKE ?";

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + cus_name + "%"); // Tìm username gần đúng
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Feedback feedback = new Feedback(
                        resultSet.getInt("feedback_id"),
                        resultSet.getInt("rating"),
                        resultSet.getString("comment"),
                        resultSet.getDate("feedback_date")
                );
                Customer customer = new Customer(
                        resultSet.getString("cus_name")
                );
                Product product = new Product(
                        resultSet.getString("pro_name"),
                        resultSet.getString("image"));
                feedbackList.add(new Object[]{feedback, customer, product});
            }
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý lỗi
        }
        return feedbackList;
    }

    public List<Object[]> searchFeedbackByProductrname(String pro_name) {
        List<Object[]> feedbackList = new ArrayList<>();

        String sql = "SELECT f.feedback_id, c.cus_name, p.pro_name, p.image, f.rating, f.comment, f.feedback_date "
                + "FROM Feedback f "
                + "JOIN Customer c ON f.cus_id = c.cus_id "
                + "JOIN Product p ON f.pro_id = p.pro_id "
                + "WHERE p.pro_name LIKE ?";

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + pro_name + "%"); // Tìm kiếm gần đúng theo tên sản phẩm
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Feedback feedback = new Feedback(
                        resultSet.getInt("feedback_id"),
                        resultSet.getInt("rating"),
                        resultSet.getString("comment"),
                        resultSet.getDate("feedback_date")
                );
                Customer customer = new Customer(
                        resultSet.getString("cus_name")
                );
                Product product = new Product(
                        resultSet.getString("pro_name"),
                        resultSet.getString("image")
                );
                feedbackList.add(new Object[]{feedback, customer, product});
            }
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý lỗi
        }
        return feedbackList;
    }

    public List<Object[]> sortFeedbackByDate(String order) {
        List<Object[]> feedbackList = new ArrayList<>();

        // Kiểm tra nếu order null hoặc không hợp lệ, mặc định dùng DESC
        if (order == null || (!order.equalsIgnoreCase("asc") && !order.equalsIgnoreCase("desc"))) {
            order = "desc"; // Mặc định lấy feedback mới nhất trước
        }

        String sql = "SELECT f.feedback_id, c.cus_name, p.pro_name, p.image, f.rating, f.comment, f.feedback_date "
                + "FROM Feedback f "
                + "JOIN Customer c ON f.cus_id = c.cus_id "
                + "JOIN Product p ON f.pro_id = p.pro_id "
                + "ORDER BY f.feedback_date " + order; // Truy vấn an toàn

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Feedback feedback = new Feedback(
                        resultSet.getInt("feedback_id"),
                        resultSet.getInt("rating"),
                        resultSet.getString("comment"),
                        resultSet.getDate("feedback_date")
                );
                Customer customer = new Customer(resultSet.getString("cus_name"));
                Product product = new Product(resultSet.getString("pro_name"), resultSet.getString("image"));
                feedbackList.add(new Object[]{feedback, customer, product});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    public List<Object[]> getFeedbackByProductName(String productname) {
        List<Object[]> feedbackList = new ArrayList<>();
        String sql = "SELECT "
                + "    p.pro_id AS product_id, "
                + "    p.pro_name AS product_name, "
//                + "    p.size, "
                + "    p.gender, "
                + "    p.brand, "
                + "    p.price, "
                + "    p.discount, "
//                + "    p.stock, "
                + "    p.status, "
                + "    p.image, "
                + "    c.cus_name AS customer_name, "
                + "    f.rating AS rating_star, "
                + "    f.comment "
                + "FROM Product p "
                + "JOIN Feedback f ON p.pro_id = f.pro_id "
                + "JOIN Customer c ON f.cus_id = c.cus_id "
                + "WHERE p.pro_name LIKE ?";

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + productname + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Feedback feedback = new Feedback(
                        resultSet.getInt("rating_star"),
                        resultSet.getString("comment")
                );

                Customer customer = new Customer(
                        resultSet.getString("customer_name")
                );

                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getBigDecimal("price"),
//                        resultSet.getInt("stock"),
                        resultSet.getString("image"),
//                        resultSet.getString("size"),
                        resultSet.getString("gender"),
                        resultSet.getString("brand"),
                        resultSet.getString("status"),
                        resultSet.getInt("discount")
                );

                feedbackList.add(new Object[]{feedback, customer, product});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    public List<Object[]> filterFeedbackByRating(int rating) {
        List<Object[]> feedbackList = new ArrayList<>();

        String sql = "SELECT f.feedback_id, c.cus_name, p.pro_name, p.image, f.rating, f.comment, f.feedback_date "
                + "FROM Feedback f "
                + "JOIN Customer c ON f.cus_id = c.cus_id "
                + "JOIN Product p ON f.pro_id = p.pro_id "
                + "WHERE f.rating = ? "
                + "ORDER BY f.feedback_date DESC"; // Mặc định hiển thị từ mới nhất

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, rating);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Feedback feedback = new Feedback(
                        resultSet.getInt("feedback_id"),
                        resultSet.getInt("rating"),
                        resultSet.getString("comment"),
                        resultSet.getDate("feedback_date")
                );
                Customer customer = new Customer(resultSet.getString("cus_name"));
                Product product = new Product(resultSet.getString("pro_name"), resultSet.getString("image"));
                feedbackList.add(new Object[]{feedback, customer, product});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    public List<Object[]> filterFeedbackByRating(int rating, String pro_name, String sortOrder) {
        List<Object[]> feedbackList = new ArrayList<>();

        String sql = "SELECT f.feedback_id, c.cus_name, p.pro_name, p.image, f.rating, f.comment, f.feedback_date "
                + "FROM Feedback f "
                + "JOIN Customer c ON f.cus_id = c.cus_id "
                + "JOIN Product p ON f.pro_id = p.pro_id "
                + "WHERE 1=1 "; // Điều kiện luôn đúng để dễ dàng thêm WHERE động

        // Nếu có rating
        if (rating > 0) {
            sql += "AND f.rating = ? ";
        }

        // Nếu có tìm kiếm theo tên sản phẩm
        if (pro_name != null && !pro_name.trim().isEmpty()) {
            sql += "AND p.pro_name LIKE ? ";
        }

        // Thêm sắp xếp theo ngày
        sql += "ORDER BY f.feedback_date " + (sortOrder != null ? sortOrder : "DESC");

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            int paramIndex = 1;

            if (rating > 0) {
                statement.setInt(paramIndex++, rating);
            }

            if (pro_name != null && !pro_name.trim().isEmpty()) {
                statement.setString(paramIndex++, "%" + pro_name + "%");
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Feedback feedback = new Feedback(
                        resultSet.getInt("feedback_id"),
                        resultSet.getInt("rating"),
                        resultSet.getString("comment"),
                        resultSet.getDate("feedback_date")
                );
                Customer customer = new Customer(resultSet.getString("cus_name"));
                Product product = new Product(resultSet.getString("pro_name"), resultSet.getString("image"));
                feedbackList.add(new Object[]{feedback, customer, product});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

    public static void main(String[] args) {
        feedbackManagementDAO dao = new feedbackManagementDAO();

        // Nhập tên sản phẩm cần test
        String testProductName = "Adidas Sportswear Essentials 3-Stripes Windbreaker HT3399 in Black";

        List<Object[]> feedbackList = dao.getFeedbackByProductName(testProductName);

        if (feedbackList.isEmpty()) {
            System.out.println("Không có feedback nào cho sản phẩm: " + testProductName);
        } else {
            for (Object[] feedback : feedbackList) {
                Feedback fb = (Feedback) feedback[0];
                Customer customer = (Customer) feedback[1];
                Product product = (Product) feedback[2];

                System.out.println("---- FEEDBACK ----");
                System.out.println("Sản phẩm: " + product.getPro_name());
                System.out.println("Khách hàng: " + customer.getCus_name());
                System.out.println("Đánh giá: " + fb.getRating() + " sao");
                System.out.println("Nhận xét: " + fb.getComment());
                System.out.println("Ngày gửi: " + fb.getFeedback_date());
                System.out.println("-------------------");
            }
        }

    }
}
