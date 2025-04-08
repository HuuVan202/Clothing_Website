package shop.controller.staff.productManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import shop.DAO.staff.productManagement.StaffProductManagementDAO;
import shop.model.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@WebServlet(name = "UpdateProductByStaffController", urlPatterns = {"/updateProductByStaff"})
@MultipartConfig
public class UpdateProductByStaffController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int pro_id = Integer.parseInt(request.getParameter("pro_id"));

            BigDecimal price = new BigDecimal(request.getParameter("price"));
            int discount = Integer.parseInt(Optional.ofNullable(request.getParameter("discount")).orElse("0"));
            int typeId = Integer.parseInt(request.getParameter("type_id"));

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

            Product product = new Product();
            product.setPro_id(pro_id);
            product.setPrice(price);
            product.setDiscount(discount);
            Type type = new Type();
            type.setType_id(typeId);
            product.setType(type);
            product.setProductSizes(productSizes);

            StaffProductManagementDAO dao = new StaffProductManagementDAO();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            
            if (dao.updateProductByStaff(product)) {
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
    
    //Handles Delete pending status product
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            try {
                int pro_id = Integer.parseInt(request.getParameter("pro_id"));
                StaffProductManagementDAO dao = new StaffProductManagementDAO();
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                
                if (dao.deletePendingProduct(pro_id)) {
                    out.print("{\"status\":\"success\",\"message\":\"Product deleted successfully.\"}");
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
