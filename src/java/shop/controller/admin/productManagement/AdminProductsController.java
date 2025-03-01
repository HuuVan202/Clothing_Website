package shop.controller.admin.productManagement;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import shop.DAO.admin.productManagement.AdminProductsDAO;
import shop.model.Product;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "AdminProductsController", urlPatterns = {"/productsManagement"})
public class AdminProductsController extends HttpServlet {

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
            out.println("<title>Servlet AdminProductsController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminProductsController at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//        List<Product> productList = productDAO.getAllProducts();
        int page = 1;  // Mặc định trang đầu tiên
        int recordsPerPage = 10; // Số sản phẩm mỗi trang
        int maxPageDisplay = 10; // Hiển thị tối đa 10 trang

        // Kiểm tra nếu có tham số "page"
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        // Truy vấn danh sách sản phẩm với giới hạn phân trang
        AdminProductsDAO productDAO = new AdminProductsDAO();
        List<Product> productList = productDAO.getProductsByPage((page - 1) * recordsPerPage, recordsPerPage);
        int totalRecords = productDAO.getTotalProductCount();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        int startPage = Math.max(1, page - maxPageDisplay / 2);
        int endPage = Math.min(totalPages, startPage + maxPageDisplay - 1);
        if (endPage - startPage < maxPageDisplay) {
            startPage = Math.max(1, endPage - maxPageDisplay + 1);
        }
        // Truyền dữ liệu sang JSP
        request.setAttribute("productList", productList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        
        // Chuyển hướng đến trang JSP
        request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
    }
//        request.setAttribute("productList", productList);
//        if (productList.isEmpty()) {
//            request.setAttribute("productListMessage", "No product found in the database.");
//        }
//        request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
//    }

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
