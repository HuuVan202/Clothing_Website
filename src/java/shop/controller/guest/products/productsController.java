package shop.controller.guest.products;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import shop.DAO.guest.products.ListTypesBrandsDAO;
import shop.DAO.guest.products.ProductsDAO;
import shop.model.Product;

@WebServlet(name = "products", urlPatterns = {"/products"})
public class productsController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ListTypesBrandsDAO tDao = new ListTypesBrandsDAO();
        ProductsDAO pDao = new ProductsDAO();

        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        if (!isAjax) {
            request.setAttribute("listT", tDao.getTypes());
            request.setAttribute("listBrand", tDao.getBrands());
        }

        String indexPage = request.getParameter("index");
        int index = (indexPage == null) ? 1 : Integer.parseInt(indexPage);
        String search = request.getParameter("search");
        String gender = request.getParameter("gender");
        String type = request.getParameter("type");
        String brand = request.getParameter("brand");
        String sort = request.getParameter("sort");

        System.out.println("Parameters - Search: " + search + ", Gender: " + gender + ", Type: " + type + ", Brand: " + brand + ", Sort: " + sort);

        // Get all products without pagination
        List<Product> listP = pDao.getAllProductsUnpaginated();

        // Apply filters and sorting sequentially
        listP = pDao.getProductsBySearch(listP, search);
        listP = pDao.getProductsByGender(listP, gender);
        listP = pDao.getProductsByType(listP, type);
        listP = pDao.getProductsByBrand(listP, brand);
        listP = pDao.sortProducts(listP, sort);

        // Apply pagination using showProducts
        List<Product> paginatedList = pDao.showProducts(listP, index);

        // Calculate total products after filtering for pagination
        int totalProduct = pDao.getTotalFilteredProducts(search, gender, type, brand);
        int endPage = totalProduct / 20;
        if (totalProduct % 20 != 0) {
            endPage++;
        }

        request.setAttribute("currentPage", index);
        request.setAttribute("endPage", endPage);

        if (isAjax) {
            if (paginatedList != null && !paginatedList.isEmpty()) {
                for (Product p : paginatedList) {
                    out.println("<div class=\"col-lg-3 col-md-4 col-sm-6 col-6 mb-4\">");
                    out.println("    <div class=\"product-card position-relative\">");
                    if (p.getDiscount() > 0) {
                        out.println("        <span class=\"discount-badge\">" + p.getDiscount() + "%</span>");
                    }
                    out.println("        <img src=\"" + p.getImage() + "\" class=\"product-img\" alt=\"" + p.getPro_name() + "\" />");
                    out.println("        <div class=\"card-body\">");
                    out.println("            <a class=\"text-decoration-none text-dark\" href=\"detail?id=" + p.getPro_id() + "\">");
                    out.println("                <h5 class=\"card-title\">" + p.getPro_name() + "</h5>");
                    out.println("            </a>");
                    out.println("            <div>");
                    if (p.getDiscount() > 0) {
                        out.println("                <div class=\"discount-price\">" + p.getFormattedDiscountedPrice() + " VND</div>");
                        out.println("                <div class=\"original-price text-decoration-line-through\">" + p.getFormattedPrice() + " VND</div>");
                    } else {
                        out.println("                <div class=\"discount-price\">" + p.getFormattedPrice() + " VND</div>");
                    }
                    out.println("            </div>");
                    out.println("        </div>");
                    out.println("    </div>");
                    out.println("</div>");
                }
            } else {
                out.println("<div class=\"col-12 d-flex justify-content-center align-items-center\" style=\"min-height: 300px;\">");
                out.println("    <p class=\"text-center text-muted fs-4\">No product found.</p>");
                out.println("</div>");
            }
            out.println("<!--JSON_START-->");
            out.println("{\"currentPage\": " + index + ", \"endPage\": " + endPage + "}");
            out.println("<!--JSON_END-->");
        } else {
            request.setAttribute("listP", paginatedList);
            request.getRequestDispatcher("jsp/guest/products.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
