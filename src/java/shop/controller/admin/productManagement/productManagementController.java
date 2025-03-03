package shop.controller.admin.productManagement;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.math.BigDecimal;
import java.util.List;
import shop.DAO.admin.productManagement.productManagementDAO;
import shop.model.Product;
import shop.model.Type;
import java.io.File;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "AdminProductsController", urlPatterns = {"/productM", "/addProduct", "/updateProduct"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1 MB
    maxFileSize = 1024 * 1024 * 10,  // 10 MB
    maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class productManagementController extends HttpServlet {

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
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1; // Nếu lỗi, quay về trang 1
            }
        }

        // Truy vấn danh sách sản phẩm với giới hạn phân trang
        productManagementDAO productDAO = new productManagementDAO();
        List<Product> productList = productDAO.getProductsByPage((page - 1) * recordsPerPage, recordsPerPage);
        List<Type> typeList = productDAO.getAllTypes();
//        request.setAttribute("typeList", typeList);
        int totalRecords = productDAO.getTotalProductCount();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        int startPage = Math.max(1, page - maxPageDisplay / 2);
        if (startPage < 0) {
            startPage = 0; // Đảm bảo OFFSET không bị âm
        }
        int endPage = Math.min(totalPages, startPage + maxPageDisplay - 1);
        if (endPage - startPage < maxPageDisplay) {
            startPage = Math.max(1, endPage - maxPageDisplay + 1);
        }
        // Truyền dữ liệu sang JSP
        request.setAttribute("productList", productList);
        request.setAttribute("typeList", typeList); // Truyền danh sách type xuống JSP
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        for (Type t : typeList) {
            System.out.println("Type ID: " + t.getType_id() + ", Name: " + t.getType_name());
        }
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        
        if ("/addProduct".equals(servletPath)) {
            handleAddProduct(request, response);
        } else if ("/updateProduct".equals(servletPath)) {
            handleUpdateProduct(request, response);
        }
    }

    private void handleAddProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            String gender = request.getParameter("gender").toLowerCase(); // Convert to lowercase
            String brand = request.getParameter("brand");
            BigDecimal price = new BigDecimal(request.getParameter("price"));

            String discountStr = request.getParameter("discount");
            int discount = (discountStr != null && !discountStr.isEmpty()) ? Integer.parseInt(discountStr) : 0;

            String stockStr = request.getParameter("stock");
            int stock = (stockStr != null && !stockStr.isEmpty()) ? Integer.parseInt(stockStr) : 0;
            String status = request.getParameter("status").toLowerCase(); // Convert to lowercase
            
            // Handle file upload
            Part filePart = request.getPart("image");
            String imagePath = null;
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = filePart.getSubmittedFileName();
                imagePath = "img/" + System.currentTimeMillis() + "_" + fileName.replaceAll("\\s+", "_");
                String uploadPath = request.getServletContext().getRealPath("") + File.separator + imagePath;
                
                // Ensure the directory exists
                File uploadDir = new File(uploadPath).getParentFile();
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // Write the file
                filePart.write(uploadPath);
            } else {
                request.setAttribute("errorMessage", "Please select an image.");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                return;
            }

            // Get type_id and validate
            String typeIdStr = request.getParameter("type_id");
            int typeId = (typeIdStr != null && !typeIdStr.isEmpty()) ? Integer.parseInt(typeIdStr) : -1;
            
            if (typeId <= 0) {
                request.setAttribute("errorMessage", "Invalid product type. Please select a valid type.");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                return;
            }

            // Handle sizes
            String[] selectedSizes = request.getParameterValues("size");
            String size;
            
            if (typeId >= 6 && typeId <= 9) {
                size = "One Size";
            } else {
                if (selectedSizes == null || selectedSizes.length == 0) {
                    request.setAttribute("errorMessage", "Please select at least one size for this product type.");
                    request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                    return;
                }
                size = String.join(", ", selectedSizes);
            }

            // Create Product object
            Product newProduct = new Product();
            newProduct.setPro_name(name);
            newProduct.setImage(imagePath);
            newProduct.setSize(size);
            newProduct.setGender(gender);
            newProduct.setBrand(brand);
            newProduct.setPrice(price);
            newProduct.setDiscount(discount);
            newProduct.setStock(stock);
            newProduct.setStatus(status);

            // Set Type
            Type type = new Type();
            type.setType_id(typeId);
            newProduct.setType(type);

            // Add product to database
            productManagementDAO productDAO = new productManagementDAO();
            System.out.println("Debug - Adding product: " + newProduct.getPro_name());
            
            boolean success = productDAO.addProduct(newProduct);
            if (success) {
                System.out.println("Debug - Product added successfully. Redirecting...");
                response.sendRedirect(request.getContextPath() + "/productsManagement");
            } else {
                request.setAttribute("errorMessage", "Failed to save product. Please try again.");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
        }
    }

    private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get product ID
            int productId = Integer.parseInt(request.getParameter("pro_id"));
            
            // Get other parameters
            String name = request.getParameter("name");
            String gender = request.getParameter("gender").toLowerCase(); // Convert to lowercase
            String brand = request.getParameter("brand");
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            
            String discountStr = request.getParameter("discount");
            int discount = (discountStr != null && !discountStr.isEmpty()) ? Integer.parseInt(discountStr) : 0;
            
            String stockStr = request.getParameter("stock");
            int stock = (stockStr != null && !stockStr.isEmpty()) ? Integer.parseInt(stockStr) : 0;
            
            String status = request.getParameter("status").toLowerCase(); // Convert to lowercase
            
            // Get existing product to preserve image if no new one is uploaded
            productManagementDAO productDAO = new productManagementDAO();
            Product existingProduct = productDAO.getProductById(productId);
            String imagePath = existingProduct.getImage(); // Default to existing image
            
            // Handle file upload if new image is provided
            Part filePart = request.getPart("image");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = filePart.getSubmittedFileName();
                imagePath = "img/" + System.currentTimeMillis() + "_" + fileName.replaceAll("\\s+", "_");
                String uploadPath = request.getServletContext().getRealPath("") + File.separator + imagePath;
                
                // Ensure the directory exists
                File uploadDir = new File(uploadPath).getParentFile();
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // Write the file
                filePart.write(uploadPath);
            }
            
            // Get type_id and validate
            String typeIdStr = request.getParameter("type_id");
            int typeId = (typeIdStr != null && !typeIdStr.isEmpty()) ? Integer.parseInt(typeIdStr) : -1;
            
            if (typeId <= 0) {
                request.setAttribute("errorMessage", "Invalid product type. Please select a valid type.");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                return;
            }
            
            // Handle sizes
            String[] selectedSizes = request.getParameterValues("size");
            String size;
            
            if (typeId >= 6 && typeId <= 9) {
                size = "One Size";
            } else {
                if (selectedSizes == null || selectedSizes.length == 0) {
                    request.setAttribute("errorMessage", "Please select at least one size for this product type.");
                    request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                    return;
                }
                size = String.join(", ", selectedSizes);
            }
            
            // Create Product object for update
            Product updatedProduct = new Product();
            updatedProduct.setPro_id(productId);
            updatedProduct.setPro_name(name);
            updatedProduct.setImage(imagePath);
            updatedProduct.setSize(size);
            updatedProduct.setGender(gender);
            updatedProduct.setBrand(brand);
            updatedProduct.setPrice(price);
            updatedProduct.setDiscount(discount);
            updatedProduct.setStock(stock);
            updatedProduct.setStatus(status);
            
            // Set Type
            Type type = new Type();
            type.setType_id(typeId);
            updatedProduct.setType(type);
            
            // Update product in database
            boolean success = productDAO.updateProduct(updatedProduct);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/productsManagement?updateSuccess=true");
            } else {
                request.setAttribute("errorMessage", "Failed to update product. Please try again.");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
        }
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
