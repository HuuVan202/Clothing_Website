/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller.customer.checkout;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import shop.DAO.customer.cart.CartDAO;
import shop.DAO.customer.cart.ProductDAO;
import shop.DAO.customer.checkout.OrderDAO;
import shop.DAO.customer.checkout.OrderDetailDAO;
import shop.model.CartItem;
import shop.model.CartUtil;
import shop.model.Customer;
import shop.model.EmailService;
import shop.model.Order;
import shop.model.OrderDetail;

/**
 *
 * @author Admin
 */
@WebServlet(name = "VNPayReturnServlet", urlPatterns = {"/vnpayreturn"})
public class VNPayReturnServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<String, String[]> paramMap = request.getParameterMap();
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            params.put(entry.getKey(), entry.getValue()[0]);
        }

        String vnp_ResponseCode = params.get("vnp_ResponseCode");
        String vnp_TransactionNo = params.get("vnp_TransactionNo");
        String vnp_Amount = params.get("vnp_Amount");
        String vnp_TxnRef = params.get("vnp_TxnRef");

        Customer customer = (Customer) session.getAttribute("customer");
        CartUtil cartUtil = (CartUtil) session.getAttribute("cart");
        List<CartItem> cart = cartUtil.getItems();

        OrderDAO orderDAO = new OrderDAO();
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        ProductDAO productDAO = new ProductDAO();

        if ("00".equals(vnp_ResponseCode)) {
            try {
                BigDecimal totalPrice = new BigDecimal(vnp_Amount).divide(new BigDecimal(100));
                Order order = new Order(0, customer.getCus_id(), totalPrice, "processing", new java.util.Date(), "bank_transfer");
                int orderId = orderDAO.InsertOrder(order);
                for (CartItem item : cart) {
                    OrderDetail orderDetail = new OrderDetail(0, orderId, item.getProduct().getPro_id(), item.getQuantity(), item.getSize(), item.getProduct().getSalePrice());
                    orderDetailDAO.insertOrderDetail(orderDetail);
                    productDAO.updateStock(item.getProduct().getPro_id(), item.getSize(), item.getQuantity());
                }
                EmailService.sendMultiProductPaymentConfirmationVNPay(customer.getEmail(), customer.getCus_name(), customer.getAddress(), totalPrice, cart);
                CartDAO cartDAO = new CartDAO();
                cartDAO.removeAllCartItems(customer.getCus_id());
                session.removeAttribute("cart");
                session.removeAttribute("size");
            } catch (SQLException ex) {
                Logger.getLogger(VNPayReturnServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        response.sendRedirect("Cart");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}