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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import shop.DAO.customer.cart.CartDAO;
import shop.DAO.customer.cart.ProductDAO;
import shop.DAO.customer.checkout.OrderDAO;
import shop.DAO.customer.checkout.OrderDetailDAO;
import shop.model.CartItem;
import shop.model.CartUtil;
import shop.model.Customer;
import shop.model.Order;
import shop.model.OrderDetail;

/**
 *
 * @author Admin
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = {"/Checkout"})
public class CheckoutServlet extends HttpServlet {

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
            out.println("<title>Servlet CheckoutServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CheckoutServlet at " + request.getContextPath() + "</h1>");
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

        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            response.sendRedirect("Login");
            return;
        }

        CartDAO cartDAO = new CartDAO();
        List<CartItem> cartItems = cartDAO.getCartItemsByCustomerId(customer.getCus_id());
        CartUtil cart = (CartUtil) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartUtil(customer.getCus_id());
        }

        cart.setItems(cartItems != null ? cartItems : new ArrayList<>());

        session.setAttribute("cart", cart);

        request.getRequestDispatcher("/jsp/customer/checkout.jsp").forward(request, response);
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
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        CartUtil cartUtil = (CartUtil) session.getAttribute("cart");

        List<CartItem> cart = cartUtil.getItems();
        if (customer == null || cart == null || cart.isEmpty()) {
            response.sendRedirect("Login");
            return;
        }

        String paymentMethod = request.getParameter("payment_method");

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem item : cart) {
            totalPrice = totalPrice.add(item.getProduct().getSalePrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        
        
        
        
        Order order = new Order(0, customer.getCus_id(), totalPrice, "processing", new Date(), paymentMethod);
        OrderDAO orderDAO = new OrderDAO();
        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
        ProductDAO productDAO = new ProductDAO();

        try {
            int orderId = orderDAO.InsertOrder(order);

            for (CartItem item : cart) {
                OrderDetail orderDetail = new OrderDetail(0, orderId, item.getProduct().getPro_id(), item.getQuantity(), item.getProduct().getSalePrice());
                orderDetailDAO.insertOrderDetail(orderDetail);
                
                productDAO.updateStock(item.getProduct().getPro_id(),item.getQuantity());
            }

            CartDAO cartDAO = new CartDAO();
            cartDAO.removeAllCartItems(customer.getCus_id());

            session.removeAttribute("cart");
            session.removeAttribute("size");

            session.setAttribute("orderMessage", "Order Successful");
            response.sendRedirect("Checkout");
                    
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("Checkout");
        }
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
