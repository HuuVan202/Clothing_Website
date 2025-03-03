/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller.guest.home;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import shop.DAO.guest.home.homeDAO;
import shop.model.Customer;
import shop.model.Product;

/**
 *
 * @author Dinh_Hau
 */
@WebServlet(name = "home", urlPatterns = {"/home"})
public class HomeController extends HttpServlet {

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
            out.println("<title>Servlet newServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet newServlet at " + request.getContextPath() + "</h1>");
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
        homeDAO dao = new homeDAO();

        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        Cookie[] cookies = request.getCookies();

        String username = "";

        // Kiểm tra nếu có Customer trong session
        if (customer != null && customer.getUsername() != null && !customer.getUsername().trim().isEmpty()) {
            username = customer.getUsername().trim();
            System.out.println("Found customer in session, username: " + username);
        } else if (cookies != null) {
            // Nếu không có trong session, kiểm tra cookie
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cUserName")) {
                    String cookieValue = cookie.getValue();
                    if (cookieValue != null && !cookieValue.trim().isEmpty()) {
                        username = cookieValue.trim();
                    }
                    System.out.println("Found username from cookie: " + username);
                    break;
                }
            }
        }

        List<Product> recommendProducts = null;

        if (!username.isEmpty()) {
            String cusId = dao.getCusIdByUsername(username);
            System.out.println("Retrieved cusId for username " + username + ": " + cusId);
            recommendProducts = dao.getRecommendProducts(cusId);
        }

        // Nếu recommendProducts vẫn null sau try-catch, đảm bảo lấy danh sách mặc định
        if (recommendProducts == null || recommendProducts.isEmpty()) {
            System.out.println("RecommendProducts is null, fetching default recommendations.");
            recommendProducts = dao.getRecommendProducts("");
        }

        System.out.println("Recommend Products size: " + (recommendProducts != null ? recommendProducts.size() : "null"));

        // Lấy danh sách sản phẩm từ DAO
        List<Product> newArrivals = dao.getNewArrivals();
        List<Product> bestSellers = dao.getBestSellers();
        List<Product> discountedProducts = dao.getDiscountedProducts();

        // Xử lý danh sách New Arrivals
        if (newArrivals != null && !newArrivals.isEmpty()) {
            request.setAttribute("newArrivalsList", newArrivals);
        } else {
            request.setAttribute("newArrivalsMessage", "No new arrivals found.");
        }

        // Xử lý danh sách Best Sellers
        if (bestSellers != null && !bestSellers.isEmpty()) {
            request.setAttribute("bestSellers", bestSellers);
        } else {
            request.setAttribute("bestSellersMessage", "No best sellers found.");
        }

        // Xử lý danh sách Discounted Products
        if (discountedProducts != null && !discountedProducts.isEmpty()) {
            request.setAttribute("discountedProducts", discountedProducts);
        } else {
            request.setAttribute("discountedProductsMessage", "No discounted products found.");
        }

        // Xử lý danh sách Recommended Products
        if (recommendProducts != null && !recommendProducts.isEmpty()) {
            request.setAttribute("recommendProducts", recommendProducts);
        } else {
            request.setAttribute("recommendProductsMessage", "No recommended products found.");
        }

        // Chuyển tiếp đến trang home.jsp
        request.getRequestDispatcher("jsp/guest/home.jsp").forward(request, response);
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
        return "Handles the home page, displaying products and recommendations for guests.";
    }// </editor-fold>

}
