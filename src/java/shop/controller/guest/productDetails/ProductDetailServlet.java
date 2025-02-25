/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller.guest.productDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import shop.DAO.guest.productDetails.FeedbackDAO;
import shop.DAO.guest.productDetails.ProductDAO;
import shop.model.Feedback;
import shop.model.Product;

@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/detail"})
public class ProductDetailServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        int id = 0;
        if (idParam != null && !idParam.isEmpty()) {
            try {
                int productId = Integer.parseInt(idParam); // Sửa lỗi gán productId

                ProductDAO productDAO = new ProductDAO();
                Product product = productDAO.getProductById(productId);

                FeedbackDAO feedbackDAO = new FeedbackDAO();
                List<Feedback> feedbacks = feedbackDAO.getFeedbackByProductId(productId); // Sửa lỗi truyền productId
                double averageRating = feedbackDAO.getAverageRating(productId);
                int feedbackCount = feedbackDAO.getFeedbackCount(productId);

                if (product != null) {
                    List<Product> suggestions = productDAO.getSuggestedProducts(product.getType_id(), product.getType_id());
                    request.setAttribute("product", product);
                    request.setAttribute("suggestions", suggestions);
                } else {
                    request.setAttribute("error", "Product not found.");
                }
                request.setAttribute("feedbacks", feedbacks);
                request.setAttribute("averageRating", averageRating);
                request.setAttribute("feedbackCount", feedbackCount);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid product ID.");
            }
        } else {
            request.setAttribute("error", "Product ID is missing.");
        }

        request.getRequestDispatcher("/jsp/guest/productDetails.jsp").forward(request, response);

    }
}
