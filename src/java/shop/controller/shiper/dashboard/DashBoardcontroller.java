/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller.shiper.dashboard;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import shop.DAO.guest.login.LoginDAO;
import shop.DAO.shiper.order.orderDAO;
import shop.DAO.shipper.profile.profileDAO;
import shop.model.Customer;

/**
 *
 * @author Admin
 */
@WebServlet(name = "DashBoardcontroller", urlPatterns = {"/DashBoardcontroller"})
public class DashBoardcontroller extends HttpServlet {

    private final String DASHBOARD = "jsp/shiper/Dashboard.jsp";
    orderDAO dAO = new orderDAO();
    profileDAO profile = new profileDAO();
    LoginDAO loginDao = new LoginDAO();

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
        HttpSession session = request.getSession();

        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            response.sendRedirect("Login");
            return;
        }

        int shippingCount = dAO.getShippingOrdersCount();

        int deleveriedCount = dAO.getCountDelivered();

        int canceledCount = dAO.getCountCanceled();

        int pendingCount = dAO.getPendingDeliCount();

        request.setAttribute("canceledCount", canceledCount);
        request.setAttribute("deleveriedCount", deleveriedCount);
        request.setAttribute("shippingCount", shippingCount);
        request.setAttribute("pendingCount", pendingCount);

        List<Customer> customers = profile.getShipper();
        request.setAttribute("customers", customers);

        request.getRequestDispatcher(DASHBOARD).forward(request, response);
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
