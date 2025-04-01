package shop.controller.guest.productDetails;

import shop.DAO.guest.productDetails.ProductDetailsDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.MultipartConfig;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "EditFeedbackController", urlPatterns = {"/editFeedback"})
@MultipartConfig
public class EditFeedbackController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            // Lấy tham số từ form
            String feedbackIdStr = request.getParameter("feedback_id");
            String ratingStr = request.getParameter("rating");
            String comment = request.getParameter("comment");

            int feedbackId = Integer.parseInt(feedbackIdStr);
            int rating = Integer.parseInt(ratingStr);

            // Gọi DAO để cập nhật feedback
            ProductDetailsDAO dao = new ProductDetailsDAO();
            boolean updated = dao.updateFeedback(feedbackId, rating, comment);

            if (updated) {
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