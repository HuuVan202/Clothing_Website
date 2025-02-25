/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller.customer.cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import shop.DAO.guest.products.productDAO;
import shop.model.CartItem;
import shop.model.CartUtil;
import shop.model.Product;

/**
 *
 * @author Admin
 */
@WebServlet(name = "cartServlet", urlPatterns = {"/cartServlet"})

/**
 *
 * @author NguyenHoangQuan
 */
public class cartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        productDAO proDAO = new productDAO();
        CartUtil cart = (CartUtil) session.getAttribute("cart");

        if (cart == null) {
            cart = new CartUtil();
        }

        String action = request.getParameter("action") == null ? "" : request.getParameter("action");

        switch (action) {
            case "add":
                String proid = request.getParameter("pro_id");

                try {
                    int id = Integer.parseInt(proid);
                    Product product = proDAO.getProductById(id);

                    if (product == null) {
                        request.setAttribute("error", "Product not found");
                        request.getRequestDispatcher("jsp/customer/cart.jsp").forward(request, response);
                        return;
                    }
                    CartItem existingItem = cart.getItemById(id);

                    if (existingItem != null) {
                        existingItem.setQuantity(existingItem.getQuantity() + 1);
                    } else {
                        CartItem item = new CartItem(product, 1);
                        cart.addItemToCart(item);
                    }

                    session.setAttribute("cart", cart);
                    session.setAttribute("size", cart.getItems().size());
                    response.sendRedirect("home");

                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid product ID");
                    request.getRequestDispatcher("jsp/cart/cart.jsp").forward(request, response);
                }
                break;

            case "delete":
                String idDelete = request.getParameter("pro_id");
                try {
                    int id = Integer.parseInt(idDelete);
                    cart.removeItem(id);

                    session.setAttribute("cart", cart);
                    session.setAttribute("size", cart.getItems().size());

                    response.sendRedirect("jsp/customer/cart.jsp");

                } catch (NumberFormatException e) {
                    request.getRequestDispatcher("jsp/customer/cart.jsp").forward(request, response);
                }
                break;

            case "updateQuantity":
                String idUpdate = request.getParameter("pro_id");
                String quantityUpdate = request.getParameter("quantity");

                try {
                    int id = Integer.parseInt(idUpdate);
                    int quantity = Integer.parseInt(quantityUpdate);

                    if (quantity <= 0) {
                        cart.removeItem(id);
                    } else {
                        cart.updateQuantity(id, quantity);
                    }
                    session.setAttribute("cart", cart);
                    session.setAttribute("size", cart.getItems().size());
                    response.sendRedirect("jsp/customer/cart.jsp");
                } catch (NumberFormatException e) {
                    request.getRequestDispatcher("jsp/customer/cart.jsp").forward(request, response);
                }
                break;

            default:
                response.sendRedirect("jsp/common/home.jsp");
                break;
        }
    }
}
