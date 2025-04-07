/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller.shiper.order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import shop.DAO.shiper.order.orderDAO;
import shop.model.Order;

/**
 *
 * @author Admin
 */
@WebServlet(name = "orderServlet", urlPatterns = {"/orderServlet"})
public class orderServlet extends HttpServlet {

    orderDAO dao = new orderDAO();

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
            out.println("<title>Servlet orderServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet orderServlet at " + request.getContextPath() + "</h1>");
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
        String action = request.getParameter("action");

        if ("updateStatus".equals(action)) {
            updateOrderStatus(request, response);
        } else if ("searchNamePending".equals(action)) {
            searchNamePending(request, response);
        } else {
            List<Order> pendingOrders = dao.getAllOrderPending();
            request.setAttribute("pendingOrders", pendingOrders);

            request.getRequestDispatcher("jsp/shiper/checkOrder.jsp").forward(request, response);
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
        String action = request.getParameter("action");

        switch (action) {
            case "updateStatus":
                updateOrderStatus(request, response);
                break;
            case "searchNamePending":
                searchNamePending(request, response);
                break;
            default:
                processRequest(request, response);
                break;
        }
    }

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("order_id"));
            String newStatus = request.getParameter("tracking");

            boolean success = dao.updateOrderStatus(orderId, newStatus);

            if (success) {
                request.getSession().setAttribute("message", "Order #" + orderId + " status updated to " + newStatus);
            } else {
                request.getSession().setAttribute("error", "Failed to update order status");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid order ID");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "System error: " + e.getMessage());
            e.printStackTrace();
        }

        response.sendRedirect("orderServlet");
    }

    private void searchNamePending(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try {
            String name = request.getParameter("cus_name");
            if (name == null) {
                name = "";
            }
            List<Order> orders = dao.searchPendingByCustomerName(name);
            request.setAttribute("pendingOrders", orders);
            request.setAttribute("name", name);
            request.getRequestDispatcher("jsp/shiper/checkOrder.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getServletInfo() {
        return "Servlet quản lý đơn hàng cho shipper";
    }

}
