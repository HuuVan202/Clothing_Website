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
import java.util.Map;
import java.util.ArrayList;
import shop.DAO.admin.productManagement.ProductManagementDAO;
import shop.model.Product;
import shop.model.ProductSize;
import shop.model.Type;
import java.io.File;
import java.util.Arrays;
import java.net.URLEncoder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "AdminProductsController", urlPatterns = {"/productM", "/addProduct", "/updateProduct"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
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
                        String productImage = request.getParameter("productImage"); // Current image path

                        // Handle new image upload if present
                        Part filePart = request.getPart("image");
                        if (filePart != null && filePart.getSize() > 0) {
                            String fileName = filePart.getSubmittedFileName();
                            if (fileName != null && !fileName.isEmpty()) {
                                // Get real path to web directory
                                String webPath = request.getServletContext().getRealPath("");
                                String relativePath = "img/product/accessories/bag/adidas/";
                                File uploadDir = new File(webPath + relativePath);
                                if (!uploadDir.exists()) {
                                    uploadDir.mkdirs();
                                }

                                // Delete old image if exists
                                if (productImage != null && !productImage.isEmpty()) {
                                    File oldFile = new File(webPath + productImage);
                                    if (oldFile.exists()) {
                                        oldFile.delete();
                                    }
                                }

                                // Save new file
                                filePart.write(uploadDir.getAbsolutePath() + File.separator + fileName);
                                productImage = relativePath + fileName;
                            }
                        }

                        int typeId = Integer.parseInt(request.getParameter("type_id"));
                        String gender = request.getParameter("gender");
                        String brand = request.getParameter("brand");
                        BigDecimal price = new BigDecimal(request.getParameter("price"));

                        // Handle discount
                        String discountStr = request.getParameter("discount");
                        int discount = 0;
                        if (discountStr != null && !discountStr.trim().isEmpty()) {
                            try {
                                discount = Integer.parseInt(discountStr);
                            } catch (NumberFormatException e) {
                                throw new ServletException("Invalid discount value");
                            }
                        }

                        // Handle status
                        String status = request.getParameter("status");
                        if (status == null || status.trim().isEmpty()) {
                            status = "active"; // Default status
                        }

                        // Handle sizes and stocks
                        List<ProductSize> productSizes = new ArrayList<>();
                        String[] stocks = request.getParameterValues("stock");

                        if (typeId >= 6 && typeId <= 9) {
                            // For accessories (one size)
                            ProductSize oneSize = new ProductSize();
                            oneSize.setPro_id(productId);
                            oneSize.setSize("One Size");
                            try {
                                oneSize.setStock(Integer.parseInt(stocks[0]));
                            } catch (NumberFormatException e) {
                                throw new ServletException("Invalid stock value for One Size product");
                            }
                            productSizes.add(oneSize);
                        } else {
                            // For clothing items (multiple sizes)
                            String[] selectedSizes = request.getParameterValues("size");
                            if (selectedSizes == null || selectedSizes.length == 0) {
                                throw new ServletException("At least one size must be selected for clothing items");
                            }

                            if (selectedSizes.length != stocks.length) {
                                throw new ServletException("Number of sizes must match number of stock values");
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

                            // Create ProductSize objects
                            for (int i = 0; i < orderedSizes.size(); i++) {
                                ProductSize ps = new ProductSize();
                                ps.setPro_id(productId);
                                ps.setSize(orderedSizes.get(i));
                                try {
                                    ps.setStock(Integer.parseInt(stocks[i]));
                                } catch (NumberFormatException e) {
                                    throw new ServletException("Invalid stock value for size " + orderedSizes.get(i));
                                }
                                productSizes.add(ps);
                            }
                        }

                        // Create and populate Product object
                        Product product = new Product();
                        product.setPro_id(productId);
                        product.setPro_name(productName);
                        product.setImage(productImage);
                        Type type = new Type();
                        type.setType_id(typeId);
                        product.setType(type);
                        product.setGender(gender);
                        product.setBrand(brand);
                        product.setPrice(price);
                        product.setDiscount(discount);
                        product.setStatus(status);
                        product.setProductSizes(productSizes);

                        ProductManagementDAO dao = new ProductManagementDAO();
                        if (dao.updateProduct(product)) {
                            response.sendRedirect("admin-products?updateSuccess=true");
                        } else {
                            response.sendRedirect("admin-products?updateError=true");
                        }
                        break;
                    case "add":
                        // Get form data
                        String addProductName = request.getParameter("productName");
                        int addTypeId = Integer.parseInt(request.getParameter("type_id"));
                        String addGender = request.getParameter("gender");
                        String addBrand = request.getParameter("brand");
                        BigDecimal addPrice = new BigDecimal(request.getParameter("price"));
                        int addDiscount = Integer.parseInt(request.getParameter("discount"));
                        String addStatus = request.getParameter("status");

                        // Handle image upload
                        Part addFilePart = request.getPart("image");
                        String addFileName = addFilePart.getSubmittedFileName();
                        String addImagePath = null;
                        if (addFileName != null && !addFileName.isEmpty()) {
                            String webPath = request.getServletContext().getRealPath("");
                            String relativePath = "img/product/accessories/bag/adidas/";
                            File uploadDir = new File(webPath + relativePath);
                            if (!uploadDir.exists()) {
                                uploadDir.mkdirs();
                            }
                            addFilePart.write(uploadDir.getAbsolutePath() + File.separator + addFileName);
                            addImagePath = relativePath + addFileName;
                        }

                        // Handle sizes and stocks
                        List<ProductSize> addProductSizes = new ArrayList<>();
                        String[] addStocks = request.getParameterValues("stock");

                        if (addTypeId >= 6 && addTypeId <= 9) {
                            // For accessories (one size)
                            ProductSize oneSize = new ProductSize();
                            oneSize.setSize("One Size");
                            try {
                                oneSize.setStock(Integer.parseInt(addStocks[0]));
                            } catch (NumberFormatException e) {
                                throw new ServletException("Invalid stock value for One Size product");
                            }
                            addProductSizes.add(oneSize);
                        } else {
                            // For clothing items (multiple sizes)
                            String[] selectedSizes = request.getParameterValues("size");
                            if (selectedSizes == null || selectedSizes.length == 0) {
                                throw new ServletException("At least one size must be selected for clothing items");
                            }

                            if (selectedSizes.length != addStocks.length) {
                                throw new ServletException("Number of sizes must match number of stock values");
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

                            // Create ProductSize objects
                            for (int i = 0; i < orderedSizes.size(); i++) {
                                ProductSize ps = new ProductSize();
                                ps.setSize(orderedSizes.get(i));
                                try {
                                    ps.setStock(Integer.parseInt(addStocks[i]));
                                } catch (NumberFormatException e) {
                                    throw new ServletException("Invalid stock value for size " + orderedSizes.get(i));
                                }
                                addProductSizes.add(ps);
                            }
                        }

                        // Create Product object
                        Product newProduct = new Product();
                        newProduct.setPro_name(addProductName);
                        newProduct.setImage(addImagePath);
                        Type addType = new Type();
                        addType.setType_id(addTypeId);
                        newProduct.setType(addType);
                        newProduct.setGender(addGender);
                        newProduct.setBrand(addBrand);
                        newProduct.setPrice(addPrice);
                        newProduct.setDiscount(addDiscount);
                        newProduct.setStatus(addStatus);
                        newProduct.setProductSizes(addProductSizes);

                        ProductManagementDAO addDao = new ProductManagementDAO();
                        if (addDao.addProduct(newProduct)) {
                            response.sendRedirect("productM?addSuccess=true");
                        } else {
                            response.sendRedirect("productM?addError=true");
                        }
                        break;
                }
            }
        } catch (ServletException e) {
            response.sendRedirect("admin-products?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin-products?error=" + URLEncoder.encode("An unexpected error occurred", "UTF-8"));
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
            if ("All Gender".equals(genderFilter)) {
                genderFilter = null;
            }
            if ("All Type".equals(typeFilter)) {
                typeFilter = null;
            }
            if ("All Brand".equals(brandFilter)) {
                brandFilter = null;
            }
            if ("All Status".equals(statusFilter)) {
                statusFilter = null;
            }
            if ("All Stock".equals(stockFilter)) {
                stockFilter = null;
            }

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

            // Convert product list to JSON
            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .disableHtmlEscaping()
                    .setPrettyPrinting()
                    .create();
            String productListJson = gson.toJson(productList);
            System.out.println("Product list size: " + productList.size()); // Debug log
            System.out.println("Generated JSON: " + productListJson); // Debug log

            // Get type list for dropdowns
            List<Type> typeList = productDAO.getAllTypes();

            // Set attributes
            request.setAttribute("productList", productList);
            request.setAttribute("productListJson", productListJson);
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

            // Add null checks for discount and status
            String discountStr = request.getParameter("discount");
            int discount = 0;
            if (discountStr != null && !discountStr.trim().isEmpty()) {
                try {
                    discount = Integer.parseInt(discountStr);
                } catch (NumberFormatException e) {
                    // Use default value of 0 if parsing fails
                }
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
            String status = request.getParameter("status");
            if (status == null || status.trim().isEmpty()) {
                status = "active"; // Default status
            }

            // Handle size and stock based on type
            List<ProductSize> productSizes = new ArrayList<>();
            int totalStock = 0;

            if (typeId >= 1 && typeId <= 5) {
                // For clothing items (multiple sizes)
                String[] sizeOrder = {"S", "M", "L", "XL", "XXL"};
                for (String size : sizeOrder) {
                    String sizeParam = request.getParameter("size_" + size);
                    if (sizeParam != null) { // If size is checked
                        String stockParam = request.getParameter("stock_" + size);
                        if (stockParam == null || stockParam.trim().isEmpty()) {
                            request.setAttribute("errorMessage", "Stock is required for size " + size);
                            request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                            return;
                        }
                        try {
                            int stockValue = Integer.parseInt(stockParam);
                            if (stockValue < 0) {
                                request.setAttribute("errorMessage", "Stock must be a positive integer for size " + size);
                                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                                return;
                            }
                            totalStock += stockValue;

                            ProductSize ps = new ProductSize();
                            ps.setSize(size);
                            ps.setStock(stockValue);
                            productSizes.add(ps);
                        } catch (NumberFormatException e) {
                            request.setAttribute("errorMessage", "Invalid stock format for size " + size);
                            request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                            return;
                        }
                    }
                }

                if (productSizes.isEmpty()) {
                    request.setAttribute("errorMessage", "Please select at least one size for clothing items");
                    request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                    return;
                }
            } else {
                // For accessories (one size)
                String stockParam = request.getParameter("stock_one_size");
                if (stockParam == null || stockParam.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "Stock is required");
                    request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                    return;
                }
                try {
                    int stockValue = Integer.parseInt(stockParam);
                    if (stockValue < 0) {
                        request.setAttribute("errorMessage", "Stock must be a positive integer for size");
                        request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                        return;
                    }
                    totalStock = stockValue;

                    ProductSize ps = new ProductSize();
                    ps.setSize("One Size");
                    ps.setStock(stockValue);
                    productSizes.add(ps);
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid stock format");
                    request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                    return;
                }
            }

            // Handle image upload
            Part filePart = request.getPart("image");
            if (filePart == null || filePart.getSize() == 0) {
                request.setAttribute("errorMessage", "Product image is required");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                return;
            }

            String fileName = getFileName(filePart);
            String uploadPath = getServletContext().getRealPath("") + File.separator + "images" + File.separator + "products";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            filePart.write(uploadPath + File.separator + fileName);
            String imageUrl = "images/products/" + fileName;

            // Create product object
            Product product = new Product();
            product.setPro_name(name);
            product.setImage(imageUrl);
            product.setGender(gender);
            product.setBrand(brand);
            Type type = new Type();
            type.setType_id(typeId);
            product.setType(type);
            product.setPrice(price);
            product.setDiscount(discount);
            product.setStatus(status);
            product.setProductSizes(productSizes);

            // Add product to database
            ProductManagementDAO dao = new ProductManagementDAO();
            if (dao.addProduct(product)) {
                response.sendRedirect("productM?success=true");
            } else {
                request.setAttribute("errorMessage", "Failed to add product");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while adding the product: " + e.getMessage());
            request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
        }
    }

    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get and validate required parameters
            int productId = Integer.parseInt(request.getParameter("pro_id"));
            String name = request.getParameter("name");
            String gender = request.getParameter("gender");
            String brand = request.getParameter("brand");
            BigDecimal price = new BigDecimal(request.getParameter("price"));

            // Add null checks for discount and status
            String discountStr = request.getParameter("discount");
            int discount = 0;
            if (discountStr != null && !discountStr.trim().isEmpty()) {
                try {
                    discount = Integer.parseInt(discountStr);
                } catch (NumberFormatException e) {
                    // Use default value of 0 if parsing fails
                }
            }

            // Add null check for stock
            String stockStr = request.getParameter("stock");
            int totalStock = 0;
            if (stockStr != null && !stockStr.trim().isEmpty()) {
                try {
                    totalStock = Integer.parseInt(stockStr);
                } catch (NumberFormatException e) {
                    // Use default value of 0 if parsing fails
                }
            }

            int typeId;
            try {
                typeId = Integer.parseInt(request.getParameter("type_id"));
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Product type is required");
                request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
                return;
            }

            // Validate required fields
            if (name == null || name.trim().isEmpty()) {
                String currentPage = request.getParameter("currentPage");
                response.sendRedirect(request.getContextPath() + "/productM?updateError=Product name is required"
                        + (currentPage != null ? "&page=" + currentPage : ""));
                return;
            }

            if (gender == null || gender.trim().isEmpty()) {
                String currentPage = request.getParameter("currentPage");
                response.sendRedirect(request.getContextPath() + "/productM?updateError=Gender is required"
                        + (currentPage != null ? "&page=" + currentPage : ""));
                return;
            }
            gender = gender.toLowerCase();

            if (brand == null || brand.trim().isEmpty()) {
                String currentPage = request.getParameter("currentPage");
                response.sendRedirect(request.getContextPath() + "/productM?updateError=Brand is required"
                        + (currentPage != null ? "&page=" + currentPage : ""));
                return;
            }

            // Validate price and stock
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                String currentPage = request.getParameter("currentPage");
                response.sendRedirect(request.getContextPath() + "/productM?updateError=Price must be greater than or equal to 0"
                        + (currentPage != null ? "&page=" + currentPage : ""));
                return;
            }

            if (totalStock < 0) {
                String currentPage = request.getParameter("currentPage");
                response.sendRedirect(request.getContextPath() + "/productM?updateError=Stock must be a positive integer"
                        + (currentPage != null ? "&page=" + currentPage : ""));
                return;
            }

            if (discount < 0 || discount > 99) {
                String currentPage = request.getParameter("currentPage");
                response.sendRedirect(request.getContextPath() + "/productM?updateError=Discount must be between 0 and 99"
                        + (currentPage != null ? "&page=" + currentPage : ""));
                return;
            }

            // Use consistent parameter name for status
            String status = request.getParameter("status");
            if (status == null || status.trim().isEmpty()) {
                status = "active"; // Default status
            }

            // Handle sizes and stocks
            List<ProductSize> productSizes = new ArrayList<>();
            if (typeId >= 6 && typeId <= 9) {
                // For accessories (one size)
                String stockParam = request.getParameter("stock_one_size");
                if (stockParam == null || stockParam.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/productM?updateError=Stock is required for accessories");
                    return;
                }
                try {
                    int stockValue = Integer.parseInt(stockParam);
                    if (stockValue < 0) {
                        response.sendRedirect(request.getContextPath() + "/productM?updateError=Stock must be a positive integer");
                        return;
                    }
                    ProductSize ps = new ProductSize();
                    ps.setSize("One Size");
                    ps.setStock(stockValue);
                    productSizes.add(ps);
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/productM?updateError=Invalid stock format");
                    return;
                }
            } else {
                // For clothing items (multiple sizes)
                String[] sizeOrder = {"S", "M", "L", "XL", "XXL"};
                boolean hasSize = false;
                
                for (String size : sizeOrder) {
                    String sizeParam = request.getParameter("size_" + size);
                    if (sizeParam != null) { // If size is checked
                        hasSize = true;
                        String stockParam = request.getParameter("stock_" + size);
                        if (stockParam == null || stockParam.trim().isEmpty()) {
                            response.sendRedirect(request.getContextPath() + "/productM?updateError=Stock is required for size " + size);
                            return;
                        }
                        try {
                            int stockValue = Integer.parseInt(stockParam);
                            if (stockValue < 0) {
                                response.sendRedirect(request.getContextPath() + "/productM?updateError=Stock must be a positive integer for size " + size);
                                return;
                            }
                            ProductSize ps = new ProductSize();
                            ps.setSize(size);
                            ps.setStock(stockValue);
                            productSizes.add(ps);
                        } catch (NumberFormatException e) {
                            response.sendRedirect(request.getContextPath() + "/productM?updateError=Invalid stock format for size " + size);
                            return;
                        }
                    }
                }
                
                if (!hasSize) {
                    response.sendRedirect(request.getContextPath() + "/productM?updateError=Please select at least one size for clothing items");
                    return;
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
            product.setGender(gender);
            product.setBrand(brand);

            Type type = new Type();
            type.setType_id(typeId);
            product.setType(type);

            product.setPrice(price);
            product.setDiscount(discount);
            product.setStatus(status);
            product.setProductSizes(productSizes);

            ProductManagementDAO productDAO = new ProductManagementDAO();
            if (productDAO.updateProduct(product)) {
                String currentPage = request.getParameter("currentPage");
                response.sendRedirect(request.getContextPath() + "/productM?updateSuccess=true"
                        + (currentPage != null ? "&page=" + currentPage : ""));
            } else {
                String currentPage = request.getParameter("currentPage");
                response.sendRedirect(request.getContextPath() + "/productM?updateError=Failed to update product"
                        + (currentPage != null ? "&page=" + currentPage : ""));
            }

        } catch (Exception e) {
            e.printStackTrace();
            String currentPage = request.getParameter("currentPage");
            response.sendRedirect(request.getContextPath() + "/productM?updateError=An error occurred while updating the product"
                    + (currentPage != null ? "&page=" + currentPage : ""));
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

