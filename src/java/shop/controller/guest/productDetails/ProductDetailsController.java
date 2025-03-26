package shop.controller.guest.productDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import shop.DAO.guest.productDetails.ProductDetailsDAO;
import shop.model.Customer;
import shop.model.Feedback;
import shop.model.Product;
import shop.model.ProductSize;

@WebServlet(name = "ProductDetailController", urlPatterns = {"/detail", "/checkPurchase", "/addFeedback"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class ProductDetailsController extends HttpServlet {

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
            out.println("<title>Servlet newServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet newServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/checkPurchase".equals(path)) {
            checkPurchaseStatus(request, response);
            return;
        }

        ProductDetailsDAO dao = new ProductDetailsDAO();

        // Lấy ID sản phẩm từ request
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

        // Lấy chi tiết sản phẩm
        Product productDetails = dao.getProductDetails(productId);
           
        List<Feedback> feedbackOfProduct = dao.getFeedBackofProduct(productId);
        List<Product> suggestProducts = dao.getSuggestProducts(productId);

        
        List<ProductSize> productSizes = dao.getSizeByProductId(productId);
        request.setAttribute("productSizes", productSizes);

        request.setAttribute("averageRating", productDetails);
        request.setAttribute("feedbackCount", productDetails);

        //      Xử lý danh sách Recommended Products
        if (productDetails != null) {
            request.setAttribute("productDetails", productDetails);
        } else {
            request.setAttribute("productDetailsMessage", "No product details found.");
        }
        //      Xử lý danh sách Recommended Products
        if (feedbackOfProduct != null && !feedbackOfProduct.isEmpty()) {
            request.setAttribute("feedbackOfProduct", feedbackOfProduct);
        } else {
            request.setAttribute("feedbackOfProductMessage", "No feedback yet.");
        }
        //      Xử lý danh sách Recommended Products
        if (suggestProducts != null && !suggestProducts.isEmpty()) {
            request.setAttribute("suggestProducts", suggestProducts);
        } else {
            request.setAttribute("suggestProductsMessage", "No recommended products found.");
        }

        request.getRequestDispatcher("jsp/guest/productDetails.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = request.getServletPath();
        switch (url) {
            case "/addFeedback":
                addFeedback(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void checkPurchaseStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

        // First check if customer has already given feedback
        if (dao.hasCustomerReviewedProduct(String.valueOf(customer.getCus_id()), productId)) {
            out.print("{\"status\":\"error\",\"message\":\"already_reviewed\"}");
            return;
        }

        // Then check if customer can give feedback (has ordered and received the product)
        if (!dao.canCustomerGiveFeedback(customer.getCus_id(), Integer.parseInt(productId))) {
            out.print("{\"status\":\"error\",\"message\":\"not_eligible\"}");
            return;
        }

        // If we get here, customer can give feedback
        out.print("{\"status\":\"success\",\"canGiveFeedback\":true}");
    }

    private void addFeedback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            // Get session and check if user is logged in
            HttpSession session = request.getSession();
            Customer customer = (Customer) session.getAttribute("customer");
            if (customer == null) {
                out.print("{\"status\":\"error\",\"message\":\"Please login to submit feedback\"}");
                return;
            }

            // Validate rating
            String ratingStr = request.getParameter("rating");
            if (ratingStr == null || ratingStr.isEmpty()) {
                out.print("{\"status\":\"error\",\"message\":\"Please select a rating\"}");
                return;
            }

            int rating = Integer.parseInt(ratingStr);
            if (rating < 1 || rating > 5) {
                out.print("{\"status\":\"error\",\"message\":\"Invalid rating value\"}");
                return;
            }

            // Validate comment
            String comment = request.getParameter("comment");
            if (comment != null && comment.length() > 500) {
                out.print("{\"status\":\"error\",\"message\":\"Comment must be less than 500 characters\"}");
                return;
            }

            // Get product ID
            int productId = Integer.parseInt(request.getParameter("pro_id"));

            // Create feedback object
            Feedback feedback = new Feedback();
            feedback.setPro_id(productId);
            feedback.setCus_id(customer.getCus_id());
            feedback.setRating(rating);
            feedback.setComment(comment);
            feedback.setFeedback_date(new java.util.Date());

            // Save feedback
            ProductDetailsDAO dao = new ProductDetailsDAO();

            // Check if user has already reviewed this product
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

    private void sendTextResponse(HttpServletResponse response, String text) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.write(text);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(500);
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.write(message);
        }
    }

    @Override
    public String getServletInfo() {
        return "Product Details Controller";
    }
}


