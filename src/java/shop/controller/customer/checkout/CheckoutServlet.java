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
import java.util.*;
import shop.DAO.customer.cart.CartDAO;
import shop.DAO.customer.cart.ProductDAO;
import shop.DAO.customer.checkout.OrderDAO;
import shop.DAO.customer.checkout.OrderDetailDAO;
import shop.model.*;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/Checkout"})
public class CheckoutServlet extends HttpServlet {

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
                    OrderDetail orderDetail = new OrderDetail(0, orderId, item.getProduct().getPro_id(), item.getQuantity(), item.getSize(), item.getProduct().getSalePrice());
                    orderDetailDAO.insertOrderDetail(orderDetail);
                    productDAO.updateStock(item.getProduct().getPro_id(), item.getSize(), item.getQuantity());
                }
                EmailService.sendMultiProductPaymentConfirmationCOD(customer.getEmail(), customer.getCus_name(), customer.getAddress(), totalPrice, cart);
                CartDAO cartDAO = new CartDAO();
                cartDAO.removeAllCartItems(customer.getCus_id());
                session.removeAttribute("cart");
                session.removeAttribute("size");
                session.setAttribute("orderMessage", "Order Successful");
                response.sendRedirect("Cart");
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("Order");
            }
        } else if ("bank_transfer".equals(paymentMethod)) {
            response.sendRedirect(processBankTransfer(request));
        }
    }

    private String processBankTransfer(HttpServletRequest request) throws IOException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
        String orderType = request.getParameter("payment_method");
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = Config.getIpAddress(request);
        String vnp_TmnCode = Config.TMN_CODE;

        BigDecimal amount = new BigDecimal(request.getParameter("amount")).multiply(new BigDecimal(100));
        String formattedAmount = amount.setScale(0, RoundingMode.HALF_UP).toPlainString();
        
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", formattedAmount);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", Config.RETURN_URL);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));
        cld.add(Calendar.MINUTE, 15);
        vnp_Params.put("vnp_ExpireDate", formatter.format(cld.getTime()));
        
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=')
                     .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII)).append('&');
            }
        }
        String vnp_SecureHash = Config.hmacSHA512(Config.HASH_SECRET, hashData.toString());
        return Config.VNPAY_URL + "?" + query.toString() + "vnp_SecureHash=" + vnp_SecureHash;
    }
}