package shop.controller.guest.productDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.google.gson.Gson;
import shop.DAO.guest.productDetails.ProductDetailsDAO;
import shop.model.Customer;
import shop.model.Feedback;
import shop.model.Product;
import shop.model.ProductSize;

@WebServlet(name = "ProductDetailController", urlPatterns = {"/detail", "/checkPurchase", "/addFeedback", "/getFeedback", "/getFeedbackByCustomer", "/editFeedback"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class ProductDetailsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/checkPurchase":
                checkPurchaseStatus(request, response);
                return;
            case "/getFeedbackByCustomer":
                getFeedbackByCustomer(request, response);
                return;
        }

        ProductDetailsDAO dao = new ProductDetailsDAO();
        String idParam = request.getParameter("id");
        int productId = 0;
        if (idParam != null && !idParam.isEmpty()) {
            try {
                productId = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Invalid product ID.");
                request.getRequestDispatcher("/jsp/guest/productDetails.jsp").forward(request, response);
                return;
            }
        }

        Product productDetails = dao.getProductDetails(productId);
        String filter = request.getParameter("filter");
        if (filter == null) {
            filter = "newest";
        }
        List<Feedback> feedbackOfProduct = dao.getFilteredFeedbackOfProduct(productId, filter);
        List<Product> suggestProducts = dao.getSuggestProducts(productId);
        List<ProductSize> productSizes = dao.getSizeByProductId(productId);

        request.setAttribute("productSizes", productSizes);

        if (productDetails != null) {
            request.setAttribute("productDetails", productDetails);
        } else {
            request.setAttribute("productDetailsMessage", "No product details found.");
        }
        if (feedbackOfProduct != null) {
            request.setAttribute("feedbackOfProduct", feedbackOfProduct);
        } else {
            request.setAttribute("feedbackOfProductMessage", "No feedback yet.");
        }
        if (suggestProducts != null && !suggestProducts.isEmpty()) {
            request.setAttribute("suggestProducts", suggestProducts);
        } else {
            request.setAttribute("suggestProductsMessage", "No suggest products found.");
        }
        request.getRequestDispatcher("jsp/guest/productDetails.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getServletPath();
        switch (url) {
            case "/addFeedback":
                addFeedback(request, response);
                break;
            case "/getFeedback":
                getFilteredFeedbackAjax(request, response);
                break;
            case "/editFeedback":
                editFeedback(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void getFeedbackByCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        String productId = request.getParameter("pro_id");

        if (customer == null || productId == null) {
            out.print("{}");
            return;
        }

        ProductDetailsDAO dao = new ProductDetailsDAO();
        Feedback fb = dao.getFeedbackByCustomer(String.valueOf(customer.getCus_id()), productId);
        Gson gson = new Gson();
        out.print(gson.toJson(fb));
    }

    private void editFeedback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            int feedbackId = Integer.parseInt(request.getParameter("feedback_id"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");

            ProductDetailsDAO dao = new ProductDetailsDAO();
            boolean success = dao.updateFeedback(feedbackId, rating, comment);

            if (success) {
                out.print("{\"status\":\"success\",\"message\":\"Feedback updated successfully\"}");
            } else {
                out.print("{\"status\":\"error\",\"message\":\"Failed to update feedback\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"status\":\"error\",\"message\":\"An error occurred\"}");
        }
    }

    private void getFilteredFeedbackAjax(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            Gson gson = new Gson();
            Feedback filterRequest = gson.fromJson(sb.toString(), Feedback.class);
            int proId = filterRequest.getPro_id();
            String filter = request.getParameter("filter") != null ? request.getParameter("filter") : "all";

            ProductDetailsDAO dao = new ProductDetailsDAO();
            List<Feedback> list = dao.getFilteredFeedbackOfProduct(proId, filter);
            out.print(gson.toJson(list));

        } catch (Exception e) {
            e.printStackTrace();
            out.print("[]");
        }
    }

    private void checkPurchaseStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String productId = request.getParameter("pro_id");
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            out.print("{\"status\":\"error\",\"message\":\"not_logged_in\"}");
            return;
        }

        ProductDetailsDAO dao = new ProductDetailsDAO();

        if (dao.hasCustomerReviewedProduct(String.valueOf(customer.getCus_id()), productId)) {
            out.print("{\"status\":\"error\",\"message\":\"already_reviewed\"}");
            return;
        }

        if (!dao.canCustomerGiveFeedback(customer.getCus_id(), Integer.parseInt(productId))) {
            out.print("{\"status\":\"error\",\"message\":\"not_eligible\"}");
            return;
        }

        out.print("{\"status\":\"success\",\"canGiveFeedback\":true}");
    }

    private void addFeedback(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

            boolean success = dao.addFeedback(String.valueOf(customer.getCus_id()), String.valueOf(productId), rating, comment);
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

    @Override
    public String getServletInfo() {
        return "Product Details Controller";
    }
}
