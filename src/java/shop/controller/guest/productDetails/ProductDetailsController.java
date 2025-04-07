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

@WebServlet(name = "ProductDetailController", urlPatterns = {"/detail", "/checkPurchase", "/getProductFeedback"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, //1 MB
        maxFileSize = 1024 * 1024 * 10, //10 MB
        maxRequestSize = 1024 * 1024 * 15 //15 MB
)
public class ProductDetailsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/checkPurchase":
                checkPurchaseStatus(request, response);
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

    @Override
    public String getServletInfo() {
        return "Product Details Controller";
    }
}
