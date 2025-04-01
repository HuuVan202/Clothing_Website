/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.shiper.order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import shop.context.DBcontext;
import shop.model.Customer;
import shop.model.Order;
import shop.model.OrderDetail;
import shop.model.Product;

/**
 *
 * @author Admin
 */
public class orderDAO {

    public static List<Order> getAllOrderPending() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, c.* FROM [dbo].[Order] o "
                + "JOIN [dbo].[Customer] c ON o.cus_id = c.cus_id "
                + "WHERE o.tracking = 'pending_delivery'";

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    int cus_id = rs.getInt("cus_id");
                    BigDecimal totalPrice = rs.getBigDecimal("total_price");
                    String tracking = rs.getString("tracking");
                    Date orderDate = rs.getDate("order_date");
                    String paymentMethod = rs.getString("payment_method");

                    Order order = new Order();
                    order.setOrder_id(orderId);
                    order.setCus_id(cus_id);
                    order.setTotal_price(totalPrice);
                    order.setTracking(tracking);
                    order.setOrder_date(orderDate);
                    order.setPayment_method(paymentMethod);

                    Customer customer = new Customer();
                    customer.setCus_name(rs.getString("cus_name"));
                    customer.setEmail(rs.getString("email"));
                    customer.setUsername(rs.getString("username"));
                    customer.setPhone(rs.getString("phone"));
                    customer.setAddress(rs.getString("address"));
                    order.setCustomer(customer);

                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static List<Order> getAllOrderShpping() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, c.* FROM [dbo].[Order] o "
                + "JOIN [dbo].[Customer] c ON o.cus_id = c.cus_id "
                + "WHERE o.tracking = 'shipping'";

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    int cus_id = rs.getInt("cus_id");
                    BigDecimal totalPrice = rs.getBigDecimal("total_price");
                    String tracking = rs.getString("tracking");
                    Date orderDate = rs.getDate("order_date");
                    String paymentMethod = rs.getString("payment_method");

                    Order order = new Order();
                    order.setOrder_id(orderId);
                    order.setCus_id(cus_id);
                    order.setTotal_price(totalPrice);
                    order.setTracking(tracking);
                    order.setOrder_date(orderDate);
                    order.setPayment_method(paymentMethod);

                    Customer customer = new Customer();
                    customer.setCus_name(rs.getString("cus_name"));
                    customer.setEmail(rs.getString("email"));
                    customer.setUsername(rs.getString("username"));
                    customer.setPhone(rs.getString("phone"));
                    customer.setAddress(rs.getString("address"));
                    order.setCustomer(customer);

                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static List<Order> getHistoryDeliveried() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, c.* \n"
                + "FROM [dbo].[Order] o \n"
                + "JOIN [dbo].[Customer] c ON o.cus_id = c.cus_id \n"
                + "WHERE o.tracking IN ('delivered', 'canceled')    ";

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    int cus_id = rs.getInt("cus_id");
                    BigDecimal totalPrice = rs.getBigDecimal("total_price");
                    String tracking = rs.getString("tracking");
                    Date orderDate = rs.getDate("order_date");
                    String paymentMethod = rs.getString("payment_method");

                    Order order = new Order();
                    order.setOrder_id(orderId);
                    order.setCus_id(cus_id);
                    order.setTotal_price(totalPrice);
                    order.setTracking(tracking);
                    order.setOrder_date(orderDate);
                    order.setPayment_method(paymentMethod);

                    Customer customer = new Customer();
                    customer.setCus_name(rs.getString("cus_name"));
                    customer.setEmail(rs.getString("email"));
                    customer.setUsername(rs.getString("username"));
                    customer.setPhone(rs.getString("phone"));
                    customer.setAddress(rs.getString("address"));
                    order.setCustomer(customer);

                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<OrderDetail> getOrderDetails(int orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT od.*,  p.pro_name, p.image  "
                + "FROM OrderDetail od "
                + "JOIN Product p ON od.pro_id = p.pro_id "
                + "WHERE od.order_id = ?";

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    OrderDetail detail = new OrderDetail();
                    detail.setOrder_detail_id(rs.getInt("order_detail_id"));
                    detail.setOrder_id(rs.getInt("order_id"));
                    detail.setPro_id(rs.getInt("pro_id"));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setPrice_per_unit(rs.getBigDecimal("price_per_unit"));
                    detail.setSize(rs.getString("size"));

                    Product product = new Product();
                    product.setPro_name(rs.getString("pro_name"));
                    product.setImage(rs.getString("image"));
                    detail.setProduct(product);

                    orderDetails.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetails;
    }

    public static boolean updateOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE [dbo].[Order] SET tracking = ? WHERE order_id = ? ";
        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newStatus);
            statement.setInt(2, orderId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getShippingOrdersCount() {
        String sql = "SELECT COUNT(*) FROM [dbo].[Order] "
                + "WHERE tracking = 'shipping'";

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountDelivered() {
        String sql = "SELECT COUNT(*) FROM [dbo].[Order] "
                + "WHERE tracking = 'delivered'";

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getPendingDeliCount() {
        String sql = "SELECT COUNT(*) FROM [dbo].[Order] "
                + "WHERE tracking = 'pending_delivery'";

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCountCanceled() {
        String sql = "SELECT COUNT(*) FROM [dbo].[Order] "
                + "WHERE tracking = 'canceled'";

        try (Connection connection = new DBcontext().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println("Danh sách chi tiết đơn hàng:");
        System.out.println("-------------------------------------");
        orderDAO dao = new orderDAO();
        int orderId = 1;
        List<OrderDetail> orderDetails = dao.getOrderDetails(orderId);

        if (orderDetails.isEmpty()) {
            System.out.println("Không tìm thấy chi tiết nào cho đơn hàng ID: " + orderId);
        } else {
            DecimalFormat currencyFormat = new DecimalFormat("###,###₫");

            System.out.println("Mã đơn hàng: " + orderDetails.get(0).getOrder_id());
            System.out.println("-------------------------------------");

            for (OrderDetail detail : orderDetails) {
                System.out.println("Mã chi tiết: " + detail.getOrder_detail_id());
                System.out.println("Sản phẩm: " + detail.getProduct().getPro_name());
                System.out.println("Hình ảnh: " + detail.getProduct().getImage());
                System.out.println("Size: " + detail.getSize());
                System.out.println("Số lượng: " + detail.getQuantity());
                System.out.println("Đơn giá: " + currencyFormat.format(detail.getPrice_per_unit()));

                BigDecimal total = detail.getPrice_per_unit().multiply(new BigDecimal(detail.getQuantity()));
                System.out.println("Thành tiền: " + currencyFormat.format(total));
                System.out.println("-------------------------------------");
            }

            System.out.println("Tổng cộng: " + orderDetails.size() + " sản phẩm trong đơn hàng");

            // Tính tổng giá trị đơn hàng
            BigDecimal orderTotal = orderDetails.stream()
                    .map(d -> d.getPrice_per_unit().multiply(new BigDecimal(d.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            System.out.println("Tổng giá trị đơn hàng: " + currencyFormat.format(orderTotal));
        }
    }
}
