/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller.admin.accountManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import shop.DAO.admin.accountManagement.accountDAO;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "filterAccountManagement", urlPatterns = {"/filterAccountManagement"})
public class filterAccountManagement extends HttpServlet {

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
            out.println("<title>Servlet filterAccountManagement</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet filterAccountManagement at " + request.getContextPath() + "</h1>");
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
    response.setContentType("text/html;charset=UTF-8");
    
    // Lấy dữ liệu từ request
    String status = request.getParameter("status");
    
    // Lưu trạng thái vào session để sử dụng khi tìm kiếm
    request.getSession().setAttribute("selectedStatus", status);
    
    // Khởi tạo DAO
    accountDAO dao = new accountDAO();
    
    // Lấy danh sách tài khoản dựa trên trạng thái
    List<Object[]> accounts = dao.getFilteredCustomerAccounts(null, status);
    
    // Gửi dữ liệu đến JSP
    if (accounts == null || accounts.isEmpty()) {
        request.setAttribute("errorMessage", "No accounts found for the selected status.");
    } else {
        request.setAttribute("accounts", accounts);
    }
    
    request.setAttribute("selectedStatus", status);
    
    request.getRequestDispatcher("jsp/admin/accountManagement.jsp").forward(request, response);
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
        doGet(request, response);
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
