/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller.guest.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import shop.DAO.guest.login.LoginDAO;
import shop.model.Account;
import shop.model.Customer;

/**
 *
 * @author Admin
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/Login"})
public class LoginServlet extends HttpServlet {

    private final String LOGIN = "jsp/guest/login.jsp";

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
        request.getRequestDispatcher(LOGIN).forward(request, response);
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
        String username = request.getParameter("txtUserName");
        String password = request.getParameter("txtPassword");
        String remember = request.getParameter("remember");

        LoginDAO loginDao = new LoginDAO();
        Account account = loginDao.checkLoginAccount(username, password);

        if (account != null) {
            // Create Session
            Customer customer = loginDao.getCustomer(account.getUsername());

            //Set customer to session
            HttpSession session = request.getSession();
            session.setAttribute("customer", customer);

            Cookie userNameCookie = new Cookie("cUserName", username);
            userNameCookie.setMaxAge(60 * 60 * 24 * 30 * 2); //3 months

            Cookie passWordCookie = new Cookie("cPassword", password);
            Cookie remCookie = new Cookie("reMem", "checked");

            if (remember != null) {
                passWordCookie.setMaxAge(60 * 60 * 24 * 30 * 2);
                remCookie.setMaxAge(60 * 60 * 24 * 30 * 2);
            } else {
                passWordCookie.setMaxAge(0);
                remCookie.setMaxAge(0);
            }

            response.addCookie(userNameCookie);
            response.addCookie(passWordCookie);
            response.addCookie(remCookie);

            response.sendRedirect("home");
        } else {
            String message = "Invalid username or password!";
            request.setAttribute("msg", message);
            request.getRequestDispatcher(LOGIN).forward(request, response);

        }
    }

}
