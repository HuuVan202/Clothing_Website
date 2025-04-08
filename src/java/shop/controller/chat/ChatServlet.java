/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller.chat;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import shop.DAO.chat.ChatDAO;
import shop.model.Account;
import shop.model.Chat;

/**
 *
 * @author Admin
 */
@WebServlet(name = "ChatServlet", urlPatterns = {"/Chat"})
public class ChatServlet extends HttpServlet {

    private final String CHAT_PATH = "jsp/customer/chat.jsp";
    private final String CHATLIST_PATH = "jsp/admin/chatList.jsp";
    private static final int ADMIN_ID = 1;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account account = (Account) request.getSession().getAttribute("admin");
        Chat chat = new Chat();
        ChatDAO chatDAO = new ChatDAO();

        if (account == null) {
            response.sendRedirect("Login");
            return;
        }
        String userRole = account.getRole();
        int userID = account.getAccountID();

        try {
            if (userRole.equals("admin") || userRole.equals("staff")) {
                Set<Integer> customerIDs = chatDAO.getAdminChatCustomers();
                request.setAttribute("customerIDs", customerIDs);

                Map<Integer, String> customerNames = new HashMap<>();
                if (customerIDs != null) {
                    for (int customerID : customerIDs) {
                        customerNames.put(customerID, chatDAO.getCustomerName(customerID));
                    }
                }   
                request.setAttribute("customerNames", customerNames);

                String customerIDParam = request.getParameter("customerID");
                if (customerIDParam != null && !customerIDParam.isEmpty()) {
                    int customerID = Integer.parseInt(customerIDParam);
                    List<Chat> chats = chatDAO.getChatsBetweenUsers(ADMIN_ID, customerID);
                    chat.setSenderID(ADMIN_ID);
                    chat.setReceiverID(customerID);
                    chatDAO.updateStatusSeen(customerID, chat);
                    request.setAttribute("chats", chats);
                    request.setAttribute("selectedCustomerID", customerID);
                }

                request.getRequestDispatcher(CHATLIST_PATH).forward(request, response);

            } else {
                List<Chat> chats = chatDAO.getChatsBetweenUsers(userID, ADMIN_ID);
                chat.setReceiverID(ADMIN_ID);
                chat.setSenderID(userID);
                request.setAttribute("chats", chats);
                chatDAO.updateStatusSeen(ADMIN_ID, chat);
                request.getRequestDispatcher(CHAT_PATH).forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(CHAT_PATH);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Set response type
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();

            // Get the user account from session
            Account account = (Account) request.getSession().getAttribute("admin");
            if (account == null || account.getRole() == null || account.getRole().trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.write("{\"success\": false, \"message\": \"User not logged in\"}");
                return;
            }

            // Get user details
            int userID = account.getAccountID();
            String userRole = account.getRole();

            // Get message content
            String messageContent = request.getParameter("messageContent");
            if (messageContent == null || messageContent.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"Message cannot be empty\"}");
                return;
            }

            // Create chat object
            Chat chat = new Chat();
            chat.setMessageContent(messageContent);
            chat.setSentDate(new Timestamp(System.currentTimeMillis()));
            chat.setIsSeen(false);

            // Set sender and receiver based on role
            if (userID == ADMIN_ID || "staff".equals(userRole)) {
                String customerIDParam = request.getParameter("customerID");
                if (customerIDParam == null || customerIDParam.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("{\"success\": false, \"message\": \"Customer ID is required for admin\"}");
                    return;
                }
                try {
                    int customerID = Integer.parseInt(customerIDParam);
                    chat.setSenderID(userID);
                    chat.setReceiverID(customerID);
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("{\"success\": false, \"message\": \"Invalid customer ID format\"}");
                    return;
                }
            } else {
                chat.setSenderID(userID);
                chat.setReceiverID(ADMIN_ID);
            }

            // Insert chat message
            ChatDAO chatDAO = new ChatDAO();
            boolean success = chatDAO.insertChat(chat);

            if (success) {
                out.write("{\"success\": true, \"message\": \"Message sent successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"success\": false, \"message\": \"Failed to send message\"}");
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }

    private int generateDialogueID(int userID) {
        return Math.abs(userID * 1000 + (int) (System.currentTimeMillis() % 1000));
    }

}
