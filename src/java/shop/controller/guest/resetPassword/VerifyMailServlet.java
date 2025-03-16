package shop.controller.guest.resetPassword;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import shop.DAO.guest.singup.SignupDAO;

/**
 *
 * @author Admin
 */
@WebServlet(urlPatterns = {"/VerifyMail"})
public class VerifyMailServlet extends HttpServlet {

    private final String PATH = "jsp/guest/verifyMail.jsp";

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
        SignupDAO checkMail = new SignupDAO();
        String mail = request.getParameter("txtEmail").trim();

        if (checkMail.checkEmailExist(mail)) {
            HttpSession session = request.getSession();
            session.setAttribute("mail", mail);
            response.sendRedirect("CheckOTP");
        } else {
            request.setAttribute("mess", "Email does not exist.");
            request.getRequestDispatcher(PATH).forward(request, response);
            return;
        }
    }

}
