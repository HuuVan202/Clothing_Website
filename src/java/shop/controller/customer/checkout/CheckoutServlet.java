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
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import shop.DAO.customer.cart.CartDAO;
import shop.DAO.customer.cart.ProductDAO;
import shop.DAO.customer.checkout.OrderDAO;
import shop.DAO.customer.checkout.OrderDetailDAO;
import shop.model.CartItem;
import shop.model.CartUtil;
import shop.model.Config;
import shop.model.Customer;
import shop.model.EmailService;
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
        if (customer == null) {
            response.sendRedirect("Login");
            return;
        } else if (cart == null || cart.isEmpty()) {
            response.sendRedirect("home");
            return;
        }

        String paymentMethod = request.getParameter("payment_method");

        if ("cash".equals(paymentMethod)) {

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

                    productDAO.updateStock(item.getProduct().getPro_id(), item.getQuantity());
                }
                EmailService.sendMultiProductPaymentConfirmationCOD(customer.getEmail(), customer.getCus_name(), customer.getAddress(), totalPrice, cart);

                CartDAO cartDAO = new CartDAO();
                cartDAO.removeAllCartItems(customer.getCus_id());

                session.removeAttribute("cart");
                session.removeAttribute("size");

                session.setAttribute("orderMessage", "Order Successful");
<<<<<<< Updated upstream
<<<<<<< Updated upstream
                response.sendRedirect("Checkout");
=======
                response.sendRedirect("Order");
>>>>>>> Stashed changes
=======
                response.sendRedirect("Order");
>>>>>>> Stashed changes
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("Order");
            }
        } else if ("bank_transfer".equals(paymentMethod)) {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");

            String orderType = request.getParameter("payment_method");
            String vnp_TxnRef = Config.getRandomNumber(8);
            String vnp_IpAddr = Config.getIpAddress(request);
            String vnp_TmnCode = Config.TMN_CODE;

            String amountParam = request.getParameter("amount");
            BigDecimal amount = new BigDecimal(amountParam);
            amount = amount.multiply(new BigDecimal(100));

            String formattedAmount = amount.setScale(0, RoundingMode.HALF_UP).toPlainString();
            Map vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", formattedAmount);
            vnp_Params.put("vnp_CurrCode", "VND");
            String bank_code = request.getParameter("bankcode");
            if (bank_code != null && !bank_code.isEmpty()) {
                vnp_Params.put("vnp_BankCode", bank_code);
            }
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.put("vnp_OrderType", orderType);

            String locate = request.getParameter("language");
            if (locate != null && !locate.isEmpty()) {
                vnp_Params.put("vnp_Locale", locate);
            } else {
                vnp_Params.put("vnp_Locale", "vn");
            }
            vnp_Params.put("vnp_ReturnUrl", Config.RETURN_URL);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());

            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
            //Billing
            vnp_Params.put("vnp_Bill_Mobile", request.getParameter("txt_billing_mobile"));
            vnp_Params.put("vnp_Bill_Email", request.getParameter("txt_billing_email"));
            String fullName = request.getParameter("txt_billing_fullname");
            if (fullName != null && !fullName.trim().isEmpty()) {
                fullName = fullName.trim();
                int idx = fullName.indexOf(' ');
                if (idx != -1) {
                    String firstName = fullName.substring(0, idx);
                    String lastName = fullName.substring(fullName.lastIndexOf(' ') + 1);
                    vnp_Params.put("vnp_Bill_FirstName", firstName);
                    vnp_Params.put("vnp_Bill_LastName", lastName);
                } else {
                    vnp_Params.put("vnp_Bill_FirstName", fullName);
                    vnp_Params.put("vnp_Bill_LastName", fullName);
                }
            } else {
                vnp_Params.put("vnp_Bill_FirstName", "N/A");
                vnp_Params.put("vnp_Bill_LastName", "N/A");
            }

            vnp_Params.put("vnp_Bill_Address", request.getParameter("txt_inv_addr1"));
            vnp_Params.put("vnp_Bill_City", request.getParameter("txt_bill_city"));
            vnp_Params.put("vnp_Bill_Country", request.getParameter("txt_bill_country"));
            if (request.getParameter("txt_bill_state") != null && !request.getParameter("txt_bill_state").isEmpty()) {
                vnp_Params.put("vnp_Bill_State", request.getParameter("txt_bill_state"));
            }
            // Invoice
            vnp_Params.put("vnp_Inv_Phone", request.getParameter("txt_inv_mobile"));
            vnp_Params.put("vnp_Inv_Email", request.getParameter("txt_inv_email"));
            vnp_Params.put("vnp_Inv_Customer", request.getParameter("txt_inv_customer"));
            vnp_Params.put("vnp_Inv_Address", request.getParameter("txt_inv_addr1"));
            vnp_Params.put("vnp_Inv_Company", request.getParameter("txt_inv_company"));
            vnp_Params.put("vnp_Inv_Taxcode", request.getParameter("txt_inv_taxcode"));
            vnp_Params.put("vnp_Inv_Type", request.getParameter("cbo_inv_type"));
            //Build data to hash and querystring
            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = Config.hmacSHA512(Config.HASH_SECRET, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = Config.VNPAY_URL + "?" + queryUrl;

            response.sendRedirect(paymentUrl);
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
