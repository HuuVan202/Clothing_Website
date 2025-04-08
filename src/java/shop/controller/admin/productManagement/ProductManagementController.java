package shop.controller.admin.productManagement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import shop.DAO.admin.productManagement.ProductManagementDAO;
import shop.model.Product;
import shop.model.Type;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductManagementController", urlPatterns = {"/productM"})
public class ProductManagementController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ProductManagementDAO productDAO = new ProductManagementDAO();

            //Filter
            String typeFilter = request.getParameter("type");
            String genderFilter = request.getParameter("gender");
            String brandFilter = request.getParameter("brand");
            String statusFilter = request.getParameter("status");
            String stockFilter = request.getParameter("stock");
            String searchQuery = request.getParameter("search");
            String sortBy = request.getParameter("sortBy");

            //Pagination
            int page = 1;
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException ignored) {}

            int itemsPerPage = 10;
            int totalProducts = productDAO.getTotalFilteredProducts(
                    typeFilter, genderFilter, brandFilter, statusFilter, stockFilter, searchQuery);
            int totalPages = (int) Math.ceil((double) totalProducts / itemsPerPage);
            if (page > totalPages && totalPages > 0) page = totalPages;

            List<Product> productList = productDAO.getFilteredAndSortedProducts(
                    typeFilter, genderFilter, brandFilter, statusFilter, stockFilter,
                    searchQuery, sortBy, page, itemsPerPage);

            // Convert to JSON
            Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().create();
            String productListJson = gson.toJson(productList);

            List<Type> typeList = productDAO.getAllTypes();
            
            //Transfer Attribute to database
            request.setAttribute("productList", productList);
            request.setAttribute("productListJson", productListJson);
            request.setAttribute("typeList", typeList);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalProducts", totalProducts);

            request.setAttribute("typeFilter", typeFilter);
            request.setAttribute("genderFilter", genderFilter);
            request.setAttribute("brandFilter", brandFilter);
            request.setAttribute("statusFilter", statusFilter);
            request.setAttribute("stockFilter", stockFilter);
            request.setAttribute("searchQuery", searchQuery);
            request.setAttribute("sortBy", sortBy);

            request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading the product list");
            request.getRequestDispatcher("/jsp/admin/productManagement.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles product management view with filtering and pagination";
    }
}
