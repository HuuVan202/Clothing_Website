package shop.controller.guest.productDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import shop.DAO.guest.productDetails.ProductDetailsDAO;
import shop.model.Product;
import shop.model.Feedback;

@WebServlet(name = "ProductDetailController", urlPatterns = {"/detail"})
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
        processRequest(request, response);
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
