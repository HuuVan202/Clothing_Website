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

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet VNPayReturnServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet VNPayReturnServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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

        String message;
        OrderDAO orderDAO = new OrderDAO();
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        ProductDAO productDAO = new ProductDAO();

        if ("00".equals(vnp_ResponseCode)) {
            try {
                BigDecimal totalPrice = new BigDecimal(vnp_Amount).divide(new BigDecimal(100));

                Order order = new Order(0, customer.getCus_id(), totalPrice, "processing", new java.util.Date(), "bank_transfer");
                int orderId = orderDAO.InsertOrder(order);
                for (CartItem item : cart) {
                    OrderDetail orderDetail = new OrderDetail(0, orderId, item.getProduct().getPro_id(), item.getQuantity(), item.getProduct().getSalePrice());
                    orderDetailDAO.insertOrderDetail(orderDetail);

                    productDAO.updateStock(item.getProduct().getPro_id(), item.getQuantity());
                }
                EmailService.sendMultiProductPaymentConfirmationVNPay(customer.getEmail(), customer.getCus_name(), customer.getAddress(), totalPrice, cart);

                CartDAO cartDAO = new CartDAO();
                cartDAO.removeAllCartItems(customer.getCus_id());

                session.removeAttribute("cart");
                session.removeAttribute("size");

            } catch (SQLException ex) {
                Logger.getLogger(VNPayReturnServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            message = "Thanh toán thất bại! Mã lỗi: " + vnp_ResponseCode;
        }
<<<<<<< Updated upstream
<<<<<<< Updated upstream
        session.setAttribute("orderMessage", "Order Successful");
        response.sendRedirect("Checkout");
=======
        response.sendRedirect("Order");
>>>>>>> Stashed changes
=======
        response.sendRedirect("Order");
>>>>>>> Stashed changes
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
