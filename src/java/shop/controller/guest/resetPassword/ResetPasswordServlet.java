/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller.guest.resetPassword;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;
import shop.DAO.customer.changePassword.ChangePasswordDAO;
import shop.DAO.guest.forgotPassword.ForgotPasswordDAO;

/**
 *
 * @author Admin
 */
@WebServlet(name = "ResetPasswordServlet", urlPatterns = {"/ResetPassword"})
public class ResetPasswordServlet extends HttpServlet {

    private final String PATH = "jsp/guest/resetPassword.jsp";

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
        request.getRequestDispatcher(PATH).forward(request, response);
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
        ChangePasswordDAO changePassDao = new ChangePasswordDAO();
        ForgotPasswordDAO forgotPass = new ForgotPasswordDAO();

        String mail = (String) request.getSession().getAttribute("mail");
        System.out.println(mail);
        String userName = forgotPass.getUsernameByMail(mail);
        System.out.println(userName);
        String password = request.getParameter("newPassword").trim();
        String rePassword = request.getParameter("rePassword").trim();
        
        if (!password.equals(rePassword)) {
            request.setAttribute("mess", "Passwords do not match! Please re-enter!");
            request.getRequestDispatcher(PATH).forward(request, response);
            return;
        } else if (!isValidPassword(password)) {
            request.setAttribute("mess", "Password must be at least 8 characters, including uppercase and special characters!");
            request.getRequestDispatcher(PATH).forward(request, response);
            return;
        }
        
        System.out.println(userName);
        //Sau khi đổi mật khẩu thì cho đăng nhập tài khoản vừa forgot vào luôn
        if (changePassDao.changePass(userName, password)) {
            System.out.println("thanh cong");
        }else{
            System.out.println("khong thanh cong");
        }
        
        response.sendRedirect("Login");

    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
        return Pattern.matches(regex, password);
    }

}
