/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller.customer.userProfile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import shop.DAO.customer.userProfile.UserProfileDAO;
import shop.DAO.guest.login.LoginDAO;
import shop.model.Customer;

/**
 *
 * @author Admin
 */
@WebServlet(name = "userProfile", urlPatterns = {"/profile"})
public class userProfileServlet extends HttpServlet {

    private final String PROFILE = "jsp/customer/userProfile.jsp";
    private final String CHECKOUT = "jsp/customer/checkout.jsp";
    private final String SHIPPER = "jsp/shiper/Dashboard.jsp";

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
        request.getRequestDispatcher(PROFILE).forward(request, response);
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
        UserProfileDAO userProfileDao = new UserProfileDAO();
        LoginDAO loginDao = new LoginDAO();
        String path = "";

        String userName = request.getParameter("txtUserName");
        String fullName = request.getParameter("txtFullName");
        String phone = request.getParameter("txtPhone");
        String address = request.getParameter("txtAddress");
        String from = request.getParameter("from");

        if (from.equalsIgnoreCase("profile")) {
            path = PROFILE;
        } else if (from.equalsIgnoreCase("checkout")) {
            path = CHECKOUT;
        } else if (from.equalsIgnoreCase("dashboardcontroller")) {
            path = SHIPPER;
        }
        if (fullName == null || fullName.trim().isEmpty()
                || userName == null || userName.trim().isEmpty()
                || phone == null || phone.trim().isEmpty()
                || address == null || address.trim().isEmpty()) {
            request.setAttribute("message", "Please fill in all information!");
            request.getRequestDispatcher(path).forward(request, response);
            return;
        } else if (!isValidPhoneNumber(phone)) {
            request.setAttribute("message", "Your phone number is invalid! Please re-enter!");
            request.getRequestDispatcher(path).forward(request, response);
            return;
        }

        if (userProfileDao.updateProfile(fullName, phone, address, userName)) {
            Customer customer = loginDao.getCustomer(userName);

            HttpSession session = request.getSession();

            session.setAttribute("customer", customer);

            request.setAttribute("message", "Update Successful!");
        } else {
            request.setAttribute("message", "Update Failed!");
        }

        request.getRequestDispatcher(path).forward(request, response);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Kiểm tra nếu phoneNumber là null hoặc rỗng
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }

        // Regex kiểm tra số điện thoại 10 chữ số, bắt đầu bằng số 0
        String regex = "^0\\d{9}$";
        return phoneNumber.matches(regex);
    }
}
