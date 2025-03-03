/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller.guest.login;

import shop.DAO.guest.login.LoginByGG;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import shop.DAO.guest.login.LoginDAO;
import shop.DAO.guest.singup.SignupDAO;
import shop.model.AccountGG;
import shop.model.Customer;

/**
 *
 * @author HuuVan
 */
@WebServlet(name = "LoginByGGServlet", urlPatterns = {"/LoginByGoogle"})
public class LoginByGGServlet extends HttpServlet {

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
        try {
            response.setContentType("text/html;charset=UTF-8");
            String code = request.getParameter("code");
            
            LoginDAO loginDao = new LoginDAO();
            LoginByGG gg = new LoginByGG();
            SignupDAO signupDAO = new SignupDAO();

            //Run debug
            //Get token
            String accessToken = gg.getToken(code);
            System.out.println(accessToken);

            //lấy infor account bằng token
            AccountGG accountGG = gg.getUserInfo(accessToken);
            System.out.println(accountGG);
            System.out.println(accountGG.getName());

            //Check email có tồn tại hay chưa
            //nếu có thì đăng nhập bằng mail đã tồn tại
            //Nếu chưa mail
            // thì lấy thuộc tính name và email chuyển tới trang signup
            // điền những thông tin có sẵn lên form của signup như name,mail còn user mặc định là mail luôn hoặc cho sửa lại
            // fix lại nếu User name tồn tại thì giữ form chỉ xoá mật khẩu thôi
            if (signupDAO.checkEmailExist(accountGG.getEmail())) {
                //nếu mà guest đăng nhập bằng GG nếu Email tồn tại thì cho đăng nhập
                Customer customer = loginDao.getCustomerByEmail(accountGG.getEmail());

                //Set customer to session
                HttpSession session = request.getSession();
                session.setAttribute("customer", customer);
                response.sendRedirect("home");
            } else {
                // nếu email chưa tồn tại thì đăng kí
                request.setAttribute("accountGG", accountGG);
                request.getRequestDispatcher("Signup").forward(request, response);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        processRequest(request, response);
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
