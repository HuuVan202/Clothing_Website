/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller.admin.feedbackManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import shop.DAO.admin.feedbackManagement.feedbackManagementDAO;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "filterFeedback", urlPatterns = {"/filterFeedback"})
public class filterFeedback extends HttpServlet {

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
            out.println("<title>Servlet filterFeedback</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet filterFeedback at " + request.getContextPath() + "</h1>");
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
     */@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String ratingParam = request.getParameter("rating");
    String pro_name = request.getParameter("pro_name");
    String sortOrder = request.getParameter("order");

    int rating = 0;
    if (ratingParam != null && !ratingParam.isEmpty()) {
        try {
            rating = Integer.parseInt(ratingParam);
        } catch (NumberFormatException e) {
            rating = 0;
        }
    }

    feedbackManagementDAO dao = new feedbackManagementDAO();
    // Gọi phương thức kết hợp tất cả điều kiện
    List<Object[]> feedback = dao.filterFeedbackByRating(rating, pro_name, sortOrder);

    // Nếu không có điều kiện nào, lấy tất cả feedback
    if (rating == 0 && (pro_name == null || pro_name.isEmpty()) && sortOrder == null) {
        feedback = dao.getAllFeedback();
    }

    request.setAttribute("feedback", feedback);
    request.setAttribute("selectedRating", ratingParam);
    request.setAttribute("search", pro_name);
    request.setAttribute("sortOrder", sortOrder != null ? sortOrder : "desc");

    request.getRequestDispatcher("jsp/admin/feedbackManagement.jsp").forward(request, response);
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
