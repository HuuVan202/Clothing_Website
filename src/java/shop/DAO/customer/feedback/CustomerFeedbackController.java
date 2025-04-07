package shop.DAO.customer.feedback;

import shop.DAO.guest.productDetails.ProductDetailsDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import shop.model.Customer;

@WebServlet(name = "CustomerFeedbackController", urlPatterns = {"/giveFeedback", "/editFeedback"})
@MultipartConfig
public class CustomerFeedbackController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/giveFeedback":
                giveFeedback(request, response);
                break;
            case "/editFeedback":
                editFeedback(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
        private void giveFeedback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session = request.getSession();
            Customer customer = (Customer) session.getAttribute("customer");
            if (customer == null) {
                out.print("{\"status\":\"error\",\"message\":\"Please login to submit feedback\"}");
                return;
            }

            int rating = Integer.parseInt(request.getParameter("rating"));
            if (rating < 1 || rating > 5) {
                out.print("{\"status\":\"error\",\"message\":\"Invalid rating value\"}");
                return;
            }

            String comment = request.getParameter("comment");
            if (comment != null && comment.length() > 500) {
                out.print("{\"status\":\"error\",\"message\":\"Comment must be less than 500 characters\"}");
                return;
            }

            int productId = Integer.parseInt(request.getParameter("pro_id"));
            ProductDetailsDAO dao = new ProductDetailsDAO();

            if (dao.hasCustomerReviewedProduct(String.valueOf(customer.getCus_id()), String.valueOf(productId))) {
                out.print("{\"status\":\"error\",\"message\":\"You have already reviewed this product\"}");
                return;
            }

            boolean success = dao.giveFeedback(String.valueOf(customer.getCus_id()), String.valueOf(productId), rating, comment);
            if (success) {
                out.print("{\"status\":\"success\",\"message\":\"Feedback submitted successfully\"}");
            } else {
                out.print("{\"status\":\"error\",\"message\":\"Failed to submit feedback\"}");
            }
        } catch (NumberFormatException e) {
            out.print("{\"status\":\"error\",\"message\":\"Invalid input format\"}");
        } catch (Exception e) {
            out.print("{\"status\":\"error\",\"message\":\"An error occurred while submitting feedback\"}");
            e.printStackTrace();
        }
    }

    private void editFeedback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(
                "application/json");
        response.setCharacterEncoding(
                "UTF-8");

        try (PrintWriter out = response.getWriter()) {
            // Lấy tham số từ form
            String feedbackIdStr = request.getParameter("feedback_id");
            String ratingStr = request.getParameter("rating");
            String comment = request.getParameter("comment");

            int feedbackId = Integer.parseInt(feedbackIdStr);
            int rating = Integer.parseInt(ratingStr);

            //Call DAO
            ProductDetailsDAO dao = new ProductDetailsDAO();
            boolean edited = dao.editFeedback(feedbackId, rating, comment);

            //Rating must be from 1 to 5
            if (rating < 1 || rating > 5) {
                out.print("{\"status\":\"error\",\"message\":\"Invalid rating value\"}");
                return;
            }

            if (edited) {
                out.print("{\"status\":\"success\", \"message\":\"Feedback updated successfully.\"}");
            } else {
                out.print("{\"status\":\"error\", \"message\":\"Failed to update feedback.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
