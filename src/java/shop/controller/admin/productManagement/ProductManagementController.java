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
import shop.DAO.admin.productManagement.ProductManagementDAO;
import shop.model.Product;
import shop.model.Type;
import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ArrayList;
import java.net.URLEncoder;

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
public class ProductManagementController extends HttpServlet {

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
        try {
            String action = request.getParameter("action");
            if (action != null) {
                switch (action) {
                    case "update":
                        int productId = Integer.parseInt(request.getParameter("productId"));
                        String productName = request.getParameter("productName");
                        String productImage = request.getParameter("productImage");
                        int typeId = Integer.parseInt(request.getParameter("type_id"));
                        String gender = request.getParameter("gender");
                        String brand = request.getParameter("brand");
                        BigDecimal price = new BigDecimal(request.getParameter("price"));
                        int discount = Integer.parseInt(request.getParameter("discount"));
                        int stock = Integer.parseInt(request.getParameter("stock"));
                        String status = request.getParameter("updateProductStatus");

                        // Handle size based on type
                        String size;
                        if (typeId >= 6 && typeId <= 9) {
                            size = "One Size";
                        } else {
                            String[] selectedSizes = request.getParameterValues("size");
                            if (selectedSizes != null && selectedSizes.length > 0) {
                                // Sort sizes in a consistent order: S, M, L, XL, XXL
                                List<String> sizeList = Arrays.asList(selectedSizes);
                                List<String> orderedSizes = new ArrayList<>();
                                String[] sizeOrder = {"S", "M", "L", "XL", "XXL"};
                                for (String s : sizeOrder) {
                                    if (sizeList.contains(s)) {
                                        orderedSizes.add(s);
                                    }
                                }
                                size = String.join(", ", orderedSizes);
                            } else {
                                throw new ServletException("At least one size must be selected for clothing items");
                            }
                        }

                        Product product = new Product();
                        product.setPro_id(productId);
                        product.setPro_name(productName);
                        product.setImage(productImage);
                        Type type = new Type();
                        type.setType_id(typeId);
                        product.setType(type);
                        product.setSize(size);
                        product.setGender(gender);
                        product.setBrand(brand);
                        product.setPrice(price);
                        product.setDiscount(discount);
                        product.setStock(stock);
                        product.setStatus(status);

                        ProductManagementDAO dao = new ProductManagementDAO();
                        if (dao.updateProduct(product)) {
                            response.sendRedirect("admin-products?updateSuccess=true");
                        } else {
                            response.sendRedirect("admin-products?updateError=true");
                        }
                        break;
                    // ... other cases
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin-products?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }

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
        try {
            ProductManagementDAO productDAO = new ProductManagementDAO();
            
            // Get filter parameters
            String typeFilter = request.getParameter("type");
            String genderFilter = request.getParameter("gender");
            String brandFilter = request.getParameter("brand");
            String statusFilter = request.getParameter("status");
            String stockFilter = request.getParameter("stock");
            String searchQuery = request.getParameter("search");
            String sortBy = request.getParameter("sortBy");
            
            // Handle "All" filters
            if ("All Gender".equals(genderFilter)) genderFilter = null;
            if ("All Type".equals(typeFilter)) typeFilter = null;
            if ("All Brand".equals(brandFilter)) brandFilter = null;
            if ("All Status".equals(statusFilter)) statusFilter = null;
            if ("All Stock".equals(stockFilter)) stockFilter = null;
            
            // Get page number
            int page = 1;
            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.isEmpty()) {
                try {
                    page = Integer.parseInt(pageStr);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
            
            // Items per page
            int itemsPerPage = 10;
            
            // Get total number of products for pagination
            int totalProducts = productDAO.getTotalFilteredProducts(
                typeFilter, genderFilter, brandFilter, statusFilter, stockFilter, searchQuery
            );

            // Calculate total pages
            int totalPages = (int) Math.ceil((double) totalProducts / itemsPerPage);

            // Adjust page number if it exceeds total pages
            if (page > totalPages && totalPages > 0) {
                page = totalPages;
            }

            // Get filtered and sorted products
            List<Product> productList = productDAO.getFilteredAndSortedProducts(
                typeFilter, genderFilter, brandFilter, statusFilter, stockFilter,
                searchQuery, sortBy, page, itemsPerPage
            );

            // Get type list for dropdowns
            List<Type> typeList = productDAO.getAllTypes();

            // Set attributes
            request.setAttribute("productList", productList);
            request.setAttribute("typeList", typeList);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalProducts", totalProducts);

            // Set filter parameters for maintaining state
            request.setAttribute("typeFilter", typeFilter);
            request.setAttribute("genderFilter", genderFilter);
            request.setAttribute("brandFilter", brandFilter);
            request.setAttribute("statusFilter", statusFilter);
            request.setAttribute("stockFilter", stockFilter);
            request.setAttribute("searchQuery", searchQuery);
            request.setAttribute("sortBy", sortBy);
            
            // Forward to JSP
            request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading the product list");
            request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
        }
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
            // Get and validate required parameters
            String name = request.getParameter("name");
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Product name is required");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                return;
            }
            
            String gender = request.getParameter("gender");
            if (gender == null || gender.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Gender is required");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                return;
            }
            gender = gender.toLowerCase();
            
            String brand = request.getParameter("brand");
            if (brand == null || brand.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Brand is required");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                return;
            }
            
            BigDecimal price;
            try {
                price = new BigDecimal(request.getParameter("price"));
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid price format");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                return;
            }
            
            int discount;
            try {
                discount = Integer.parseInt(request.getParameter("discount"));
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid discount format");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                return;
            }
            
            int stock;
            try {
                stock = Integer.parseInt(request.getParameter("stock"));
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid stock format");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                return;
            }
            
            int typeId;
            try {
                typeId = Integer.parseInt(request.getParameter("type_id"));
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Product type is required");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                return;
            }
            
            // Set default status to active for new products
            String status = "active";
            
            // Validate price and stock
            if (price.compareTo(BigDecimal.ZERO) <= 0 || stock <= 0) {
                request.setAttribute("errorMessage", "Price and stock must be greater than 0");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                return;
            }
            
            // Handle size based on type
            String size;
            if (typeId >= 1 && typeId <= 5) {
                String[] selectedSizes = request.getParameterValues("size");
                if (selectedSizes == null || selectedSizes.length == 0) {
                    request.setAttribute("errorMessage", "Please select at least one size for clothing items");
                    request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                    return;
                }
                // Sort sizes in a consistent order: S, M, L, XL, XXL
                List<String> sizeList = Arrays.asList(selectedSizes);
                List<String> orderedSizes = new ArrayList<>();
                String[] sizeOrder = {"S", "M", "L", "XL", "XXL"};
                for (String s : sizeOrder) {
                    if (sizeList.contains(s)) {
                        orderedSizes.add(s);
                    }
                }
                size = String.join(", ", orderedSizes);
            } else {
                size = "One Size";
            }
            
            // Handle file upload
            Part filePart = request.getPart("image");
            String imagePath = null;
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = filePart.getSubmittedFileName();
                imagePath = "img/products/" + System.currentTimeMillis() + "_" + fileName.replaceAll("\\s+", "_");
                String uploadPath = request.getServletContext().getRealPath("") + File.separator + imagePath;
                
                File uploadDir = new File(uploadPath).getParentFile();
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                filePart.write(uploadPath);
            } else {
                request.setAttribute("errorMessage", "Please select an image");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                return;
            }
            
            // Create and save product
            Product product = new Product();
            product.setPro_name(name);
            product.setImage(imagePath);
            product.setSize(size);
            product.setGender(gender);
            product.setBrand(brand);
            
            Type type = new Type();
            type.setType_id(typeId);
            product.setType(type);
            
            product.setPrice(price);
            product.setDiscount(discount);
            product.setStock(stock);
            product.setStatus(status);
            
            ProductManagementDAO productDAO = new ProductManagementDAO();
            if (productDAO.addProduct(product)) {
                String currentPage = request.getParameter("currentPage");
                response.sendRedirect(request.getContextPath() + "/productM?addSuccess=true" + 
                    (currentPage != null ? "&page=" + currentPage : ""));
            } else {
                request.setAttribute("errorMessage", "Failed to add product");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while adding the product");
            request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
        }
    }

    private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get and validate required parameters
            int productId = Integer.parseInt(request.getParameter("pro_id"));
            String name = request.getParameter("name");
            String gender = request.getParameter("gender");
            String brand = request.getParameter("brand");
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            int discount = Integer.parseInt(request.getParameter("discount"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            String status = request.getParameter("updateProductStatus");
            int typeId = Integer.parseInt(request.getParameter("type_id"));
            
            // Validate required fields
            if (name == null || name.trim().isEmpty()) {
                String currentPage = request.getParameter("currentPage");
                response.sendRedirect(request.getContextPath() + "/productM?updateError=Product name is required" + 
                    (currentPage != null ? "&page=" + currentPage : ""));
                return;
            }
            
            if (gender == null || gender.trim().isEmpty()) {
                String currentPage = request.getParameter("currentPage");
                response.sendRedirect(request.getContextPath() + "/productM?updateError=Gender is required" + 
                    (currentPage != null ? "&page=" + currentPage : ""));
                return;
            }
            gender = gender.toLowerCase();
            
            if (brand == null || brand.trim().isEmpty()) {
                String currentPage = request.getParameter("currentPage");
                response.sendRedirect(request.getContextPath() + "/productM?updateError=Brand is required" + 
                    (currentPage != null ? "&page=" + currentPage : ""));
                return;
            }
            
            // Validate price and stock
            if (price.compareTo(BigDecimal.ZERO) <= 0 || stock <= 0) {
                String currentPage = request.getParameter("currentPage");
                response.sendRedirect(request.getContextPath() + "/productM?updateError=Price and stock must be greater than 0" + 
                    (currentPage != null ? "&page=" + currentPage : ""));
                return;
            }
            
            if (discount < 0 || discount > 100) {
                String currentPage = request.getParameter("currentPage");
                response.sendRedirect(request.getContextPath() + "/productM?updateError=Discount must be between 0 and 100" + 
                    (currentPage != null ? "&page=" + currentPage : ""));
                return;
            }
            
            // Handle size based on type
            String size = request.getParameter("size");
            if (size == null || size.trim().isEmpty()) {
                if (typeId >= 1 && typeId <= 5) {
                    String currentPage = request.getParameter("currentPage");
                    response.sendRedirect(request.getContextPath() + "/productM?updateError=Please select at least one size for clothing items" + 
                        (currentPage != null ? "&page=" + currentPage : ""));
                    return;
                } else {
                    size = "One Size";
                }
            }
            
            // Handle image update
            String imagePath = request.getParameter("currentImage"); // Get current image path
            Part filePart = request.getPart("image"); // Get new image if uploaded
            
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = filePart.getSubmittedFileName();
                if (fileName != null && !fileName.isEmpty()) {
                    // Generate new image path
                    imagePath = "img/products/" + System.currentTimeMillis() + "_" + fileName.replaceAll("\\s+", "_");
                    String uploadPath = request.getServletContext().getRealPath("") + File.separator + imagePath;
                    
                    // Create directory if it doesn't exist
                    File uploadDir = new File(uploadPath).getParentFile();
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }
                    
                    // Write new image
                    filePart.write(uploadPath);
                    
                    // Delete old image if it exists and is different from default
                    String oldImagePath = request.getParameter("currentImage");
                    if (oldImagePath != null && !oldImagePath.isEmpty() 
                            && !oldImagePath.equals("img/default.jpg")
                            && !oldImagePath.equals(imagePath)) {
                        File oldFile = new File(request.getServletContext().getRealPath("") + File.separator + oldImagePath);
                        if (oldFile.exists() && oldFile.isFile()) {
                            oldFile.delete();
                        }
                    }
                }
            }
            
            // Create and update product
            Product product = new Product();
            product.setPro_id(productId);
            product.setPro_name(name);
            product.setImage(imagePath);
            product.setSize(size);
            product.setGender(gender);
            product.setBrand(brand);
            
            Type type = new Type();
            type.setType_id(typeId);
            product.setType(type);
            
            product.setPrice(price);
            product.setDiscount(discount);
            product.setStock(stock);
            product.setStatus(status);
            
            ProductManagementDAO productDAO = new ProductManagementDAO();
            if (productDAO.updateProduct(product)) {
                String currentPage = request.getParameter("currentPage");
                response.sendRedirect(request.getContextPath() + "/productM?updateSuccess=true" + 
                    (currentPage != null ? "&page=" + currentPage : ""));
            } else {
                String currentPage = request.getParameter("currentPage");
                response.sendRedirect(request.getContextPath() + "/productM?updateError=Failed to update product" + 
                    (currentPage != null ? "&page=" + currentPage : ""));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            String currentPage = request.getParameter("currentPage");
            response.sendRedirect(request.getContextPath() + "/productM?updateError=An error occurred while updating the product" + 
                (currentPage != null ? "&page=" + currentPage : ""));
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
