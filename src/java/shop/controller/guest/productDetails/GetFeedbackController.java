/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package shop.controller.guest.productDetails;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import shop.DAO.guest.productDetails.ProductDetailsDAO;
import shop.model.Feedback;
import com.google.gson.Gson;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "GetFeedbackController", urlPatterns = {"/getFeedback"})
public class GetFeedbackController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("GetFeedbackController: Received request");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String filter = request.getParameter("filter");
            String proIdStr = request.getParameter("pro_id");
            
            System.out.println("GetFeedbackController: filter=" + filter + ", pro_id=" + proIdStr);
            
            if (proIdStr == null || proIdStr.trim().isEmpty()) {
                System.out.println("GetFeedbackController: Missing pro_id parameter");
                out.print("[]");
                return;
            }
            
            // Get current customer ID from session
            
            int proId = Integer.parseInt(proIdStr);
            ProductDetailsDAO dao = new ProductDetailsDAO();
            List<Feedback> feedbackList = dao.getFilteredFeedbackOfProduct(proId, filter);
            
            System.out.println("GetFeedbackController: Found " + (feedbackList != null ? feedbackList.size() : 0) + " feedback items");
            
            if (feedbackList == null || feedbackList.isEmpty()) {
                out.print("[]");
                return;
            }
            
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(feedbackList);
            System.out.println("GetFeedbackController: JSON response = " + jsonResponse);
            out.print(jsonResponse);
            
        } catch (NumberFormatException e) {
            System.out.println("GetFeedbackController: Invalid pro_id format - " + e.getMessage());
            out.print("[]");
        } catch (Exception e) {
            System.out.println("GetFeedbackController: Error - " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}