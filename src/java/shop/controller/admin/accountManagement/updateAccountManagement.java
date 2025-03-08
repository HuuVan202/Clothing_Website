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
import shop.DAO.admin.accountManagement.accountDAO;
import shop.model.Account;
import shop.model.Customer;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "updateAccountManagement", urlPatterns = {"/updateAccountManagement"})
public class updateAccountManagement extends HttpServlet {

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
            out.println("<title>Servlet updataAccountManagement</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet updataAccountManagement at " + request.getContextPath() + "</h1>");
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
        String id_raw = request.getParameter("cus_id");
        int id = -1;

        try {
            id = Integer.parseInt(id_raw);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("accountManagement.jsp?error=InvalidID");
            return;
        }

        accountDAO dao = new accountDAO();
        Object[] account = dao.getAccountById(id);

        if (account != null) {
            request.setAttribute("account", account);
            request.getRequestDispatcher("/jsp/admin/updateAccount.jsp").forward(request, response);
        } else {
            response.sendRedirect("accountManagement.jsp?error=AccountNotFound");
        }
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
        String cusIdRaw = request.getParameter("cus_id");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String status = request.getParameter("acc_status");

        int cusId = -1;
        try {
            cusId = Integer.parseInt(cusIdRaw);
        } catch (NumberFormatException e) {
            response.sendRedirect("jsp/admin/accountManagement.jsp?error=Invalid ID");
            return;
        }

        accountDAO dao = new accountDAO();
        Object[] accountData = dao.getAccountById(cusId);
        if (accountData != null) {
            Customer customer = (Customer) accountData[0];
            Account account = (Account) accountData[1];

            customer.setEmail(email);
            customer.setPhone(phone);
            customer.setAddress(address);
            account.setAcc_status(status);

            dao.updateCustomer(customer);
            dao.updateAccount(account);

            response.sendRedirect("accountHome?");
        } else {
            response.sendRedirect("accountManagement.jsp?error=Account Not Found");
        }
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
