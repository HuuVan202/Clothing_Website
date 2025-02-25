/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller.guest.singup;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;
import shop.DAO.guest.singup.SignupDAO;
import shop.model.Account;
import shop.model.Customer;

/**
 *
 * @author HuuVan
 */
@WebServlet(name = "SignupServlet", urlPatterns = {"/Signup"})
public class SignupServlet extends HttpServlet {
    
     private final String SIGNUP = "jsp/guest/signup.jsp";

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
       request.getRequestDispatcher(SIGNUP).forward(request, response);
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
        String fullName = request.getParameter("txtFullName");
        String userName = request.getParameter("txtUserName");
        String passWord = request.getParameter("txtPassword");
        String passwordSecond = request.getParameter("txtPasswordSecond");
        String email = request.getParameter("txtEmail");
        String phone = request.getParameter("txtPhone");
        String address = request.getParameter("txtAddress");

        SignupDAO signupDAO = new SignupDAO();
        String message = null;

        if (fullName == null || fullName.trim().isEmpty()
                || userName == null || userName.trim().isEmpty()
                || passWord == null || passWord.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || phone == null || phone.trim().isEmpty()
                || address == null || address.trim().isEmpty()) {
            message = "Please fill in all information!";
        } // Check if account exists
        else if (signupDAO.checkAccountExist(userName)) {
            message = "Account already exists!";
        } // Check email exists
        else if (signupDAO.checkEmailExist(email)) {
            message = "Email already exists!";
        }else if(!passWord.equals(passwordSecond)){
            message = "Passwords do not match! Please re-enter!";
        }else if (!isValidPassword(passWord)) { // Check strong passwords
            message = "Password must be at least 8 characters, including uppercase and special characters!";
        }
        
        if (message != null) {
            request.setAttribute("message", message);
            request.getRequestDispatcher(SIGNUP).forward(request, response);
            return;
        }

        Account account = new Account(userName, passWord, "customer", "active");
        Customer customer = new Customer(fullName, email, userName, phone, address);
        if (signupDAO.signUp(customer, account)) {
            response.sendRedirect("Login");
        } else {
            request.setAttribute("message", "Registration failed, please try again!");
            request.getRequestDispatcher(SIGNUP).forward(request, response);
        }
    }

      private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
        return Pattern.matches(regex, password);
    }
     
}
