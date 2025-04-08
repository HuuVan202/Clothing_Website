package shop.controller.admin.productManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import shop.DAO.admin.productManagement.ProductManagementDAO;
import shop.model.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@WebServlet(name = "UpdateProductController", urlPatterns = {"/updateProduct"})
@MultipartConfig
public class UpdateProductController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int pro_id = Integer.parseInt(request.getParameter("pro_id"));
            String name = request.getParameter("name");
            String gender = request.getParameter("gender").toLowerCase();
            String brand = request.getParameter("brand");
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            int discount = Integer.parseInt(Optional.ofNullable(request.getParameter("discount")).orElse("0"));
            int typeId = Integer.parseInt(request.getParameter("type_id"));
            String status = Optional.ofNullable(request.getParameter("status")).orElse("active");

            // Handle size & stock
            List<ProductSize> productSizes = new ArrayList<>();

            if (typeId >= 1 && typeId <= 5) {
                String[] sizeOrder = {"S", "M", "L", "XL", "XXL"};
                for (String size : sizeOrder) {
                    if (request.getParameter("size_" + size) != null) {
                        int stock = Integer.parseInt(request.getParameter("stock_" + size));
                        ProductSize ps = new ProductSize();
                        ps.setPro_id(pro_id);
                        ps.setSize(size);
                        ps.setStock(stock);
                        productSizes.add(ps);
                    }
                }
            } else {
                int stock = Integer.parseInt(request.getParameter("stock_one_size"));
                ProductSize ps = new ProductSize();
                ps.setPro_id(pro_id);
                ps.setSize("One Size");
                ps.setStock(stock);
                productSizes.add(ps);
            }

            // Handle image update
            String imagePath = request.getParameter("currentImage");
            Part filePart = request.getPart("image");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = filePart.getSubmittedFileName();
                imagePath = "img/product/added_image/" + System.currentTimeMillis() + "_" + fileName;
                String uploadPath = getServletContext().getRealPath("") + File.separator + imagePath;
                filePart.write(uploadPath);
            }

            Product product = new Product();
            product.setPro_id(pro_id);
            product.setPro_name(name);
            product.setGender(gender);
            product.setBrand(brand);
            product.setImage(imagePath);
            product.setPrice(price);
            product.setDiscount(discount);
            product.setStatus(status);
            Type type = new Type();
            type.setType_id(typeId);
            product.setType(type);
            product.setProductSizes(productSizes);

            ProductManagementDAO dao = new ProductManagementDAO();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            if (dao.updateProduct(product)) {
                out.print("{\"status\":\"success\",\"message\":\"Product updated successfully.\"}");
            } else {
                out.print("{\"status\":\"error\",\"message\":\"Product cannot be updated.\"}");
            }
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print("{\"status\":\"error\",\"message\":\"Error: " + e.getMessage().replace("\"", "'") + "\"}");
            out.flush();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            try {
                int pro_id = Integer.parseInt(request.getParameter("pro_id"));
                ProductManagementDAO dao = new ProductManagementDAO();
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                
                if (dao.deletePendingProduct(pro_id)) {
                    out.print("{\"status\":\"success\",\"message\":\"Prodcut deleted successfully.\"}");
                } else {
                    out.print("{\"status\":\"error\",\"message\":\"Product cannot be deleted.\"}");
                }
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print("{\"status\":\"error\",\"message\":\"Error: " + e.getMessage().replace("\"", "'") + "\"}");
                out.flush();
            }
        }
    }
}
