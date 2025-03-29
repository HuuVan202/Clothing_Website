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
import java.io.PrintWriter;
import java.util.List;
import shop.DAO.customer.cart.CartDAO;
import shop.DAO.customer.cart.ProductDAO;
import shop.model.CartItem;
import shop.model.CartUtil;
import shop.model.Customer;
import shop.model.Product;

/**
 *
 * @author Admin
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/Cart"})
public class CartServlet extends HttpServlet {

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
            out.println("<title>Servlet CartServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CartServlet at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher("jsp/customer/cart.jsp").forward(request, response);
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

        HttpSession session = request.getSession();
        ProductDAO proDAO = new ProductDAO();
        Customer customer = (Customer) session.getAttribute("customer");

        CartUtil cart = (CartUtil) session.getAttribute("cart");
        if (customer == null) {
            response.sendRedirect("Login");
            return;
        }

        if (cart == null) {
            cart = new CartUtil(customer.getCus_id());
            session.setAttribute("cart", cart);
        }

        String action = request.getParameter("action") == null ? "" : request.getParameter("action");

        switch (action) {
            case "add":
                String proid = request.getParameter("pro_id");
<<<<<<< Updated upstream
=======
                String size = request.getParameter("size");
                String quantity = request.getParameter("quantity");
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes

                if (cart != null) {
                    try {
                        int id = Integer.parseInt(proid);
                        int quantityAdd = Integer.parseInt(quantity);
<<<<<<< Updated upstream

                        Product product = proDAO.getProductById(id);

                        List<ProductSize> productSizes = proDAO.getSizeByProductId(id);
                        session.setAttribute("productSizes", productSizes);

                        int stock = proDAO.getStockBySize(id, size);
                        if (stock <= 0) {
                            session.setAttribute("error", "The product is out of stock.");
                            response.sendRedirect("detail?id=" + id);
                            return;
                        }

                        if (product == null) {
                            request.setAttribute("error", "Product not found");
                            request.getRequestDispatcher("jsp/customer/cart.jsp").forward(request, response);
                            return;
                        }

                        CartItem existingItem = cart.getItems().stream()
                                .filter(i -> i.getProduct().getPro_id() == id && i.getSize().equals(size))
                                .findFirst().orElse(null);

                        if (existingItem != null) {
                            existingItem.setQuantity(existingItem.getQuantity() + quantityAdd);
                            cart.updateItemToCart(id, size, existingItem.getQuantity());
                            CartDAO.addCartItem(customer.getCus_id(), existingItem);

                        } else {
                            CartItem newItem = new CartItem(product, quantityAdd, size);
                            cart.addItemToCart(newItem);
                            CartDAO.addItem(customer.getCus_id(), newItem);
                        }

                        session.setAttribute("cart", cart);
                        session.setAttribute("size", cart.getItems().size());
                        session.setAttribute("successMessage", "Product has been added to cart!");

<<<<<<< Updated upstream
                    if (product.getStock() <= 0) {
                        session.setAttribute("error", "The product is out of stock.");
=======
>>>>>>> Stashed changes
=======

                        Product product = proDAO.getProductById(id);

                        List<ProductSize> productSizes = proDAO.getSizeByProductId(id);
                        session.setAttribute("productSizes", productSizes);

                        int stock = proDAO.getStockBySize(id, size);
                        if (stock <= 0) {
                            session.setAttribute("error", "The product is out of stock.");
                            response.sendRedirect("detail?id=" + id);
                            return;
                        }

                        if (product == null) {
                            request.setAttribute("error", "Product not found");
                            request.getRequestDispatcher("jsp/customer/cart.jsp").forward(request, response);
                            return;
                        }

                        CartItem existingItem = cart.getItems().stream()
                                .filter(i -> i.getProduct().getPro_id() == id && i.getSize().equals(size))
                                .findFirst().orElse(null);

                        if (existingItem != null) {
                            existingItem.setQuantity(existingItem.getQuantity() + quantityAdd);
                            cart.updateItemToCart(id, size, existingItem.getQuantity());
                            CartDAO.addCartItem(customer.getCus_id(), existingItem);

                        } else {
                            CartItem newItem = new CartItem(product, quantityAdd, size);
                            cart.addItemToCart(newItem);
                            CartDAO.addItem(customer.getCus_id(), newItem);
                        }

                        session.setAttribute("cart", cart);
                        session.setAttribute("size", cart.getItems().size());
                        session.setAttribute("successMessage", "Product has been added to cart!");

>>>>>>> Stashed changes
                        response.sendRedirect("detail?id=" + id);
                    } catch (NumberFormatException e) {
                        request.setAttribute("error", "Invalid product ID");
                        request.getRequestDispatcher("jsp/customer/cart.jsp").forward(request, response);
                    }
<<<<<<< Updated upstream
<<<<<<< Updated upstream

                    CartItem existingItem = cart.getItems().stream()
                            .filter(i -> i.getProduct().getPro_id() == id)
                            .findFirst().orElse(null);

                    if (existingItem != null) {
                        existingItem.setQuantity(existingItem.getQuantity() + 1);
                        cart.updateItemToCart(id, existingItem.getQuantity());
                        CartDAO.addCartItem(customer.getCus_id(), existingItem);
                    } else {
                        CartItem newItem = new CartItem(product, 1);
                        cart.addItemToCart(newItem);
                        CartDAO.saveCartToDatabase(customer.getCus_id(), cart);
                    }

                    session.setAttribute("cart", cart);

                    session.setAttribute("size", cart.getItems().size());

                    session.setAttribute("successMessage", "Product has been added to cart!");

                    response.sendRedirect("detail?id=" + id);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid product ID");
                    request.getRequestDispatcher("jsp/customer/cart.jsp").forward(request, response);
                }
                break;
=======
                    break;
                }
>>>>>>> Stashed changes
=======
                    break;
                }
>>>>>>> Stashed changes

            case "delete":
                String idDelete = request.getParameter("pro_id");
                try {
                    int id = Integer.parseInt(idDelete);
                    cart.removeItemToCart(id);

                    CartDAO.removeCartItem(customer.getCus_id(), id);

                    session.setAttribute("cart", cart);
                    session.setAttribute("size", cart.getItems().size());
                    response.sendRedirect("Cart");

                } catch (NumberFormatException e) {
                    request.getRequestDispatcher("jsp/customer/cart.jsp").forward(request, response);
                }
                break;

            case "updateQuantity":
                String idUpdate = request.getParameter("pro_id");
                String quantityUpdate = request.getParameter("quantity");

                try {
<<<<<<< Updated upstream
                    int productId = Integer.parseInt(idUpdate);
                    int quantity = Integer.parseInt(quantityUpdate);
=======
                    int id = Integer.parseInt(proId);
                    int quantityUp = Integer.parseInt(quantityStr);
                    String sizeUpdate = request.getParameter("size");

                    CartDAO.updateCartItem(customer.getCus_id(), id, sizeUpdate, quantityUp);

                    for (CartItem item : cart.getItems()) {
                        if (item.getProduct().getPro_id() == id && item.getSize().equals(sizeUpdate)) {
                            item.setQuantity(quantityUp);
                            break;
                        }
                    }
>>>>>>> Stashed changes

                    cart.updateItemToCart(productId, quantity);
                    CartDAO.updateCartItem(customer.getCus_id(), productId, quantity);
                    session.setAttribute("cart", cart);
                    session.setAttribute("size", cart.getItems().size());
                    response.sendRedirect("Cart");

                } catch (NumberFormatException e) {
                    request.setAttribute("error", "error");
                }

                break;

            default:
                response.sendRedirect("jsp/common/home.jsp");
                break;
        }

//        session.setAttribute("cart", cart);
//        
//        session.setAttribute("size", cart.getItems().size());
        //   request.getRequestDispatcher("jsp/guest/productDetail.jsp").forward(request, response);
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
