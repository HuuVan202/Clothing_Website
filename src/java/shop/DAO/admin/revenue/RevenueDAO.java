/*
 * Click nbvs://netbeans.org/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbvs://netbeans.org/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.DAO.admin.revenue;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import shop.context.DBcontext;
import shop.model.OrderDetail;

/**
 *
 * @author Dinh_Hau
 */
public class RevenueDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public int getOrdersByTimeRange(String range) {
        int orderCount = 0;
        try {
            conn = new DBcontext().getConnection();

            String sql = "SELECT COUNT(*) AS order_count FROM [Order] WHERE order_date >= DATEADD(day, ?, CAST(GETDATE() AS DATE)) AND tracking = 'delivered'";
            System.out.println("Executing SQL: " + sql);
            ps = conn.prepareStatement(sql);

            // Set the number of days based on the range
            int daysToSubtract = 0;
            switch (range.trim().toLowerCase()) {
                case "today":
                    daysToSubtract = 0; // No subtraction, but we can filter by date equality if needed
                    sql = "SELECT COUNT(*) AS order_count FROM [Order] WHERE CAST(order_date AS DATE) = CAST(GETDATE() AS DATE) AND tracking = 'delivered'";
                    ps = conn.prepareStatement(sql);
                    break;
                case "last7days":
                    daysToSubtract = -7;
                    break;
                case "last30days":
                    daysToSubtract = -30;
                    break;
                case "last3months":
                    sql = "SELECT COUNT(*) AS order_count FROM [Order] WHERE order_date >= DATEADD(month, -3, CAST(GETDATE() AS DATE)) AND tracking = 'delivered'";
                    ps = conn.prepareStatement(sql);
                    break;
                case "last6months":
                    sql = "SELECT COUNT(*) AS order_count FROM [Order] WHERE order_date >= DATEADD(month, -6, CAST(GETDATE() AS DATE)) AND tracking = 'delivered'";
                    ps = conn.prepareStatement(sql);
                    break;
                case "lastyear":
                    // Filter for the previous calendar year (e.g., 2024 if current year is 2025)
                    sql = "SELECT COUNT(*) AS order_count "
                            + "FROM [Order] o "
                            + "WHERE o.order_date >= DATEFROMPARTS(YEAR(GETDATE()) - 1, 1, 1) "
                            + "AND o.order_date < DATEFROMPARTS(YEAR(GETDATE()), 1, 1) "
                            + "AND o.tracking = 'delivered'";
                    ps = conn.prepareStatement(sql);
                    break;
                default:
                    sql = "SELECT COUNT(*) AS order_count FROM [Order] WHERE tracking = 'delivered'";
                    ps = conn.prepareStatement(sql);
                    break;
            }

            // If daysToSubtract is set, use the parameterized query
            if (daysToSubtract != 0) {
                ps.setInt(1, daysToSubtract);
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                orderCount = rs.getInt("order_count");
                System.out.println("Total orders retrieved for " + range + ": " + orderCount);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving orders: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return orderCount;
    }

    public int getSoldProductsByTimeRange(String range) {
        int totalQuantity = 0;

        try {
            conn = new DBcontext().getConnection();

            // Base SQL query with parameterized date range
            String sql = "SELECT SUM(od.quantity) AS total_quantity "
                    + "FROM [OrderDetail] od "
                    + "JOIN [Order] o ON od.order_id = o.order_id "
                    + "WHERE o.order_date >= DATEADD(day, ?, CAST(GETDATE() AS DATE)) AND o.tracking = 'delivered'";
            System.out.println("Executing SQL: " + sql);
            ps = conn.prepareStatement(sql);

            // Set the number of days based on the range
            int daysToSubtract = 0;

            switch (range.trim().toLowerCase()) {
                case "today":
                    // Filter for exact current date
                    sql = "SELECT SUM(od.quantity) AS total_quantity "
                            + "FROM [OrderDetail] od "
                            + "JOIN [Order] o ON od.order_id = o.order_id "
                            + "WHERE CAST(o.order_date AS DATE) = CAST(GETDATE() AS DATE) AND o.tracking = 'delivered'";
                    ps = conn.prepareStatement(sql);
                    break;
                case "last7days":
                    daysToSubtract = -7;
                    break;
                case "last30days":
                    daysToSubtract = -30;
                    break;
                case "last3months":
                    sql = "SELECT SUM(od.quantity) AS total_quantity "
                            + "FROM [OrderDetail] od "
                            + "JOIN [Order] o ON od.order_id = o.order_id "
                            + "WHERE o.order_date >= DATEADD(month, -3, CAST(GETDATE() AS DATE)) AND o.tracking = 'delivered'";
                    ps = conn.prepareStatement(sql);
                    break;
                case "last6months":
                    sql = "SELECT SUM(od.quantity) AS total_quantity "
                            + "FROM [OrderDetail] od "
                            + "JOIN [Order] o ON od.order_id = o.order_id "
                            + "WHERE o.order_date >= DATEADD(month, -6, CAST(GETDATE() AS DATE)) AND o.tracking = 'delivered'";
                    ps = conn.prepareStatement(sql);
                    break;
                case "lastyear":
                    // Filter for the previous calendar year (e.g., 2024 if current year is 2025)
                    sql = "SELECT SUM(od.quantity) AS total_quantity "
                            + "FROM [OrderDetail] od "
                            + "JOIN [Order] o ON od.order_id = o.order_id "
                            + "WHERE o.order_date >= DATEFROMPARTS(YEAR(GETDATE()) - 1, 1, 1) "
                            + "AND o.order_date < DATEFROMPARTS(YEAR(GETDATE()), 1, 1) "
                            + "AND o.tracking = 'delivered'";
                    ps = conn.prepareStatement(sql);
                    break;
                default:
                    sql = "SELECT SUM(od.quantity) AS total_quantity "
                            + "FROM [OrderDetail] od "
                            + "JOIN [Order] o ON od.order_id = o.order_id "
                            + "WHERE o.tracking = 'delivered'";
                    ps = conn.prepareStatement(sql);
                    break;
            }

            System.out.println("Executing SQL: " + sql);
            ps = conn.prepareStatement(sql);

            // Set parameter if using day-based range
            if (daysToSubtract != 0) {
                ps.setInt(1, daysToSubtract);
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                totalQuantity = rs.getInt("total_quantity");
                System.out.println("Total products sold for " + range + ": " + totalQuantity);
            } else {
                // If no rows are returned (e.g., no orders in range), set total to 0
                totalQuantity = 0;
            }
        } catch (Exception e) {
            System.out.println("Error retrieving sold products: " + e.getMessage());
            e.printStackTrace();
        }
        return totalQuantity;
    }

    public String getRevenueSummaryByTimeRange(String range) {
        OrderDetail o = new OrderDetail();
        BigDecimal totalRevenue = BigDecimal.ZERO; // Initialize to 0

        try {
            conn = new DBcontext().getConnection();

            // Base SQL query with parameterized date range and exclusion of canceled orders
            String sql = "SELECT SUM(o.total_price) AS total_revenue "
                    + "FROM [Order] o "
                    + "WHERE o.order_date >= DATEADD(day, ?, CAST(GETDATE() AS DATE)) "
                    + "AND o.tracking = 'delivered'";
            System.out.println("Executing SQL: " + sql);
            ps = conn.prepareStatement(sql);

            // Set the number of days based on the range
            int daysToSubtract = 0;

            switch (range.trim().toLowerCase()) {
                case "today":
                    // Filter for exact current date
                    sql = "SELECT SUM(o.total_price) AS total_revenue "
                            + "FROM [Order] o "
                            + "WHERE CAST(o.order_date AS DATE) = CAST(GETDATE() AS DATE) "
                            + "AND o.tracking = 'delivered'";
                    ps = conn.prepareStatement(sql);
                    break;
                case "last7days":
                    daysToSubtract = -7;
                    break;
                case "last30days":
                    daysToSubtract = -30;
                    break;
                case "last3months":
                    sql = "SELECT SUM(o.total_price) AS total_revenue "
                            + "FROM [Order] o "
                            + "WHERE o.order_date >= DATEADD(month, -3, CAST(GETDATE() AS DATE)) "
                            + "AND o.tracking = 'delivered'";
                    ps = conn.prepareStatement(sql);
                    break;
                case "last6months":
                    sql = "SELECT SUM(o.total_price) AS total_revenue "
                            + "FROM [Order] o "
                            + "WHERE o.order_date >= DATEADD(month, -6, CAST(GETDATE() AS DATE)) "
                            + "AND o.tracking = 'delivered'";
                    ps = conn.prepareStatement(sql);
                    break;
                case "lastyear":
                    // Filter for the previous calendar year (e.g., 2024 if current year is 2025)
                    sql = "SELECT SUM(o.total_price) AS total_revenue "
                            + "FROM [Order] o "
                            + "WHERE o.order_date >= DATEFROMPARTS(YEAR(GETDATE()) - 1, 1, 1) "
                            + "AND o.order_date < DATEFROMPARTS(YEAR(GETDATE()), 1, 1) "
                            + "AND o.tracking = 'delivered'";
                    ps = conn.prepareStatement(sql);
                    break;
                default:
                    sql = "SELECT SUM(o.total_price) AS total_revenue "
                            + "FROM [Order] o "
                            + "WHERE o.tracking = 'delivered'";
                    ps = conn.prepareStatement(sql);
                    break;
            }

            System.out.println("Executing SQL: " + sql);
            ps = conn.prepareStatement(sql);

            // Set parameter if using day-based range
            if (daysToSubtract != 0) {
                ps.setInt(1, daysToSubtract);
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                // Use getBigDecimal to retrieve the sum directly as BigDecimal
                totalRevenue = rs.getBigDecimal("total_revenue");
                // Handle null case (if no rows match, SUM returns NULL)
                if (totalRevenue == null) {
                    totalRevenue = BigDecimal.ZERO;
                }
                System.out.println("Total revenue for " + range + ": " + totalRevenue);
            } else {
                // If no rows are returned (e.g., no non-canceled orders in range), set total to 0
                totalRevenue = BigDecimal.ZERO;
            }
        } catch (Exception e) {
            System.out.println("Error retrieving revenue summary: " + e.getMessage());
            e.printStackTrace();
        }
        return o.getFormattedPrice(totalRevenue);
    }

    public List<Map<String, Object>> getMonthlyRevenueThisMonth() {
        List<Map<String, Object>> revenueData = new ArrayList<>();
        try {
            conn = new DBcontext().getConnection();
            String sql = "SELECT "
                    + "    DATENAME(MONTH, m.month_start) + ' ' + CAST(YEAR(m.month_start) AS VARCHAR) AS month, "
                    + "    COALESCE(SUM(o.total_price), 0) AS revenue "
                    + "FROM (SELECT DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1) AS month_start) AS m "
                    + "LEFT JOIN [Order] o "
                    + "    ON o.order_date >= m.month_start "
                    + "    AND o.order_date < DATEADD(MONTH, 1, m.month_start) "
                    + "    AND o.tracking = 'delivered' "
                    + "GROUP BY m.month_start";
            System.out.println("Executing SQL: " + sql);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> dataPoint = new HashMap<>();
                dataPoint.put("month", rs.getString("month"));
                BigDecimal revenue = rs.getBigDecimal("revenue");
                dataPoint.put("revenue", revenue != null ? revenue : BigDecimal.ZERO);
                revenueData.add(dataPoint);
                System.out.println("Monthly revenue for " + rs.getString("month") + ": " + revenue);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving monthly revenue for this month: " + e.getMessage());
            e.printStackTrace();
        }
        return revenueData;
    }

    public List<Map<String, Object>> getMonthlyRevenueLast3Months() {
        List<Map<String, Object>> revenueData = new ArrayList<>();
        try {
            conn = new DBcontext().getConnection();
            String sql = "WITH MonthList AS ("
                    + "    SELECT DATEADD(MONTH, -n, DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1)) AS month_start "
                    + "    FROM (SELECT 3 AS n UNION ALL SELECT 2 UNION ALL SELECT 1) AS numbers"
                    + "), "
                    + "MonthLabels AS ("
                    + "    SELECT DATENAME(MONTH, month_start) + ' ' + CAST(YEAR(month_start) AS VARCHAR) AS month, "
                    + "           month_start "
                    + "    FROM MonthList"
                    + ") "
                    + "SELECT ml.month, COALESCE(SUM(o.total_price), 0) AS revenue "
                    + "FROM MonthLabels ml "
                    + "LEFT JOIN [Order] o "
                    + "    ON o.order_date >= ml.month_start "
                    + "    AND o.order_date < DATEADD(MONTH, 1, ml.month_start) "
                    + "    AND o.tracking = 'delivered' "
                    + "GROUP BY ml.month, ml.month_start "
                    + "ORDER BY ml.month_start";
            System.out.println("Executing SQL: " + sql);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> dataPoint = new HashMap<>();
                dataPoint.put("month", rs.getString("month"));
                BigDecimal revenue = rs.getBigDecimal("revenue");
                dataPoint.put("revenue", revenue != null ? revenue : BigDecimal.ZERO);
                revenueData.add(dataPoint);
                System.out.println("Monthly revenue for " + rs.getString("month") + ": " + revenue);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving monthly revenue for last 3 months: " + e.getMessage());
            e.printStackTrace();
        }
        return revenueData;
    }

    public List<Map<String, Object>> getMonthlyRevenueLast6Months() {
        List<Map<String, Object>> revenueData = new ArrayList<>();
        try {
            conn = new DBcontext().getConnection();
            String sql = "WITH MonthList AS ("
                    + "    SELECT DATEADD(MONTH, -n, DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1)) AS month_start "
                    + "    FROM (SELECT 6 AS n UNION ALL SELECT 5 UNION ALL SELECT 4 UNION ALL SELECT 3 UNION ALL SELECT 2 UNION ALL SELECT 1) AS numbers"
                    + "), "
                    + "MonthLabels AS ("
                    + "    SELECT DATENAME(MONTH, month_start) + ' ' + CAST(YEAR(month_start) AS VARCHAR) AS month, "
                    + "           month_start "
                    + "    FROM MonthList"
                    + ") "
                    + "SELECT ml.month, COALESCE(SUM(o.total_price), 0) AS revenue "
                    + "FROM MonthLabels ml "
                    + "LEFT JOIN [Order] o "
                    + "    ON o.order_date >= ml.month_start "
                    + "    AND o.order_date < DATEADD(MONTH, 1, ml.month_start) "
                    + "    AND o.tracking = 'delivered' "
                    + "GROUP BY ml.month, ml.month_start "
                    + "ORDER BY ml.month_start";
            System.out.println("Executing SQL: " + sql);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> dataPoint = new HashMap<>();
                dataPoint.put("month", rs.getString("month"));
                BigDecimal revenue = rs.getBigDecimal("revenue");
                dataPoint.put("revenue", revenue != null ? revenue : BigDecimal.ZERO);
                revenueData.add(dataPoint);
                System.out.println("Monthly revenue for " + rs.getString("month") + ": " + revenue);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving monthly revenue for last 6 months: " + e.getMessage());
            e.printStackTrace();
        }
        return revenueData;
    }

    public List<Map<String, Object>> getMonthlyRevenueLastYear() {
        List<Map<String, Object>> revenueData = new ArrayList<>();
        try {
            conn = new DBcontext().getConnection();
            String sql = "WITH MonthList AS ("
                    + "    SELECT DATEADD(MONTH, -n, DATEFROMPARTS(YEAR(GETDATE()), MONTH(GETDATE()), 1)) AS month_start "
                    + "    FROM (SELECT 12 AS n UNION ALL SELECT 11 UNION ALL SELECT 10 UNION ALL SELECT 9 UNION ALL SELECT 8 UNION ALL "
                    + "          SELECT 7 UNION ALL SELECT 6 UNION ALL SELECT 5 UNION ALL SELECT 4 UNION ALL SELECT 3 UNION ALL "
                    + "          SELECT 2 UNION ALL SELECT 1) AS numbers"
                    + "), "
                    + "MonthLabels AS ("
                    + "    SELECT DATENAME(MONTH, month_start) + ' ' + CAST(YEAR(month_start) AS VARCHAR) AS month, "
                    + "           month_start "
                    + "    FROM MonthList"
                    + ") "
                    + "SELECT ml.month, COALESCE(SUM(o.total_price), 0) AS revenue "
                    + "FROM MonthLabels ml "
                    + "LEFT JOIN [Order] o "
                    + "    ON o.order_date >= ml.month_start "
                    + "    AND o.order_date < DATEADD(MONTH, 1, ml.month_start) "
                    + "    AND o.tracking = 'delivered' "
                    + "GROUP BY ml.month, ml.month_start "
                    + "ORDER BY ml.month_start";
            System.out.println("Executing SQL: " + sql);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> dataPoint = new HashMap<>();
                dataPoint.put("month", rs.getString("month"));
                BigDecimal revenue = rs.getBigDecimal("revenue");
                dataPoint.put("revenue", revenue != null ? revenue : BigDecimal.ZERO);
                revenueData.add(dataPoint);
                System.out.println("Monthly revenue for " + rs.getString("month") + ": " + revenue);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving monthly revenue for last year: " + e.getMessage());
            e.printStackTrace();
        }
        return revenueData;
    }

    public static void main(String[] args) {
        RevenueDAO dao = new RevenueDAO();
//        String abc = dao.getRevenueSummaryByTimeRange("last30Days");
//        OrderDetail o = new OrderDetail();
        System.out.println(dao.getMonthlyRevenueThisMonth());
    }
}
