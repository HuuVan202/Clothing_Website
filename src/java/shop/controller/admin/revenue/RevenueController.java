package shop.controller.admin.revenue;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import shop.DAO.admin.revenue.RevenueDAO;
import shop.model.OrderDetail;

@WebServlet(name = "RevenueController", urlPatterns = {"/revenue"})
public class RevenueController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        RevenueDAO dao = new RevenueDAO();
        OrderDetail orderDetail = new OrderDetail();

        String path = request.getServletPath();
        if (path.equals("/revenue")) {
            String timeRange = request.getParameter("timeRange");
            String chart = request.getParameter("chart");

            if (chart != null && !chart.isEmpty()) {
                // Handle chart data request
                List<Map<String, Object>> revenueData = null;

                try {
                    switch (timeRange) {
                        case "thisMonths":
                            revenueData = dao.getMonthlyRevenueThisMonth();
                            break;
                        case "last3months":
                            revenueData = dao.getMonthlyRevenueLast3Months();
                            break;
                        case "last6months":
                            revenueData = dao.getMonthlyRevenueLast6Months();
                            break;
                        case "lastyear":
                            revenueData = dao.getMonthlyRevenueLastYear();
                            break;
                        default:
                            revenueData = dao.getMonthlyRevenueThisMonth(); // Default to this month
                    }

                    // Build HTML response for chart data
                    StringBuilder html = new StringBuilder();
                    html.append("<div id='chartData'>");
                    html.append("<div id='labels' style='display:none;'>");
                    for (int i = 0; i < revenueData.size(); i++) {
                        html.append("<span class='label'>").append(revenueData.get(i).get("month")).append("</span>");
                    }
                    html.append("</div>");
                    html.append("<div id='values' style='display:none;'>");
                    for (int i = 0; i < revenueData.size(); i++) {
                        BigDecimal revenue = (BigDecimal) revenueData.get(i).get("revenue");
                        html.append("<span class='value'>").append(revenue != null ? revenue.toString() : "0").append("</span>");
                    }
                    html.append("</div>");
                    html.append("</div>");
                    out.print(html.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    out.print("<div class='error'>Error loading chart data: " + e.getMessage() + "</div>");
                }
                return;
            }

            // Existing logic for summary data
            if (timeRange == null || timeRange.isEmpty()) {
                timeRange = "today"; // Default to "today"
            }

            int orderCount = 0;
            int soldProducts = 0;
            String revenueSummary = "";

            try {
                orderCount = dao.getOrdersByTimeRange(timeRange);
                soldProducts = dao.getSoldProductsByTimeRange(timeRange);
                revenueSummary = dao.getRevenueSummaryByTimeRange(timeRange);
            } catch (Exception e) {
                e.printStackTrace();
                out.print("<div class='error'>Error loading revenue data: " + e.getMessage() + "</div>");
                return;
            }

            boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
            if (isAjax) {
                StringBuilder html = new StringBuilder();
                html.append("<div id='ordersContent'><div class='card-body'><h5 class='card-title'>")
                        .append(orderCount).append(" Orders</h5></div></div>");
                html.append("<div id='soldProductsContent'><div class='card-body'><h5 class='card-title'>")
                        .append(soldProducts).append(" Products</h5></div></div>");
                html.append("<div id='revenueSummaryContent'><div class='card-body'><h5 class='card-title'>")
                        .append(revenueSummary).append(" VNĐ</h5></div></div>");
                out.print(html.toString());
            } else {
                request.setAttribute("orderCount", orderCount);
                request.setAttribute("soldProducts", soldProducts);
                request.setAttribute("revenueSummary", revenueSummary + " VNĐ");
                request.getRequestDispatcher("/jsp/admin/revenue.jsp").forward(request, response);
            }
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
