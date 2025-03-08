/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller.customer.orders;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import shop.DAO.customer.orders.MyOrderDAO;
import shop.DAO.customer.orders.MyOrderDetailDAO;
import shop.model.Customer;
import shop.model.Order;
import shop.model.OrderDetail;
import shop.model.Product;

/**
 *
 * @author Admin
 */
@WebServlet(name = "OrderDetailServlet", urlPatterns = {"/OrderDetail"})
public class OrderDetailServlet extends HttpServlet {

    private final String ORDERDETAIL = "jsp/customer/orderdetails.jsp";

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
        MyOrderDetailDAO orderDetailsDao = new MyOrderDetailDAO();
        MyOrderDAO orderDao = new MyOrderDAO();
        HttpSession session = request.getSession();
        Customer cus = (Customer) session.getAttribute("customer");
        boolean checkOrderId = false;

        int id = Integer.parseInt(request.getParameter("id"));

        for (Order order : orderDao.getListOrderByID(cus.getCus_id())) {
            if (order.getOrder_id() == id) {
                checkOrderId = true;
                break;
            }
        }

        if (!checkOrderId) {
            response.sendRedirect("Error");
            return;
        }

        List<OrderDetail> listOrderDetail = orderDetailsDao.getListOrderDetail(id);

        Map<Integer, Product> listProduct = new HashMap<>();
        for (OrderDetail o : listOrderDetail) {
            int productId = o.getPro_id();
            if (!listProduct.containsKey(productId)) { // Tránh truy vấn nhiều lần
                listProduct.put(productId, orderDetailsDao.getProductByID(productId));
            }
        }

        request.setAttribute("listProduct", listProduct);
        request.setAttribute("listOrderDetail", listOrderDetail);
        request.getRequestDispatcher(ORDERDETAIL).forward(request, response);
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
}
