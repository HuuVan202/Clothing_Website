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

@WebServlet(name = "AddProductController", urlPatterns = {"/addProduct"})
@MultipartConfig
public class AddProductController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            String gender = request.getParameter("gender").toLowerCase();
            String brand = request.getParameter("brand");
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            int discount = Integer.parseInt(Optional.ofNullable(request.getParameter("discount")).orElse("0"));
            int typeId = Integer.parseInt(request.getParameter("type_id"));
            String status = Optional.ofNullable(request.getParameter("status")).orElse("active");

            // Handle size & stock
            List<ProductSize> productSizes = new ArrayList<>();
            int totalStock = 0;

            if (typeId >= 1 && typeId <= 5) {
                String[] sizeOrder = {"S", "M", "L", "XL", "XXL"};
                for (String size : sizeOrder) {
                    if (request.getParameter("size_" + size) != null) {
                        int stock = Integer.parseInt(request.getParameter("stock_" + size));
                        ProductSize ps = new ProductSize();
                        ps.setSize(size);
                        ps.setStock(stock);
                        totalStock += stock;
                        productSizes.add(ps);
                    }
                }
            } else {
                int stock = Integer.parseInt(request.getParameter("stock_one_size"));
                ProductSize ps = new ProductSize();
                ps.setSize("One Size");
                ps.setStock(stock);
                totalStock = stock;
                productSizes.add(ps);
            }

            // Handle image upload
            Part filePart = request.getPart("image");
            String fileName = filePart.getSubmittedFileName();
            String uploadPath = getServletContext().getRealPath("") + File.separator + "img/product/added_image/";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();
            filePart.write(uploadPath + File.separator + fileName);
            String imageUrl = "img/product/added_image/" + fileName;

            // Create Product
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

            // Save
            ProductManagementDAO dao = new ProductManagementDAO();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            
            if (dao.addProduct(product)) {
                out.print("{\"status\":\"success\",\"message\":\"Product added successfully.\"}");
            } else {
                out.print("{\"status\":\"error\",\"message\":\"Product cannot be added.\"}");
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
