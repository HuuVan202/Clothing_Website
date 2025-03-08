/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package shop.Filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import shop.model.Account;

/**
 *
 * @author Admin
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/Dashboard", "/accountHome",
    "/feedbackHome"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        if (session.getAttribute("admin") == null) {
            res.sendRedirect(req.getContextPath() + "/Login");
        } else {
            Account account = (Account) session.getAttribute("admin");
            if (!account.getRole().equalsIgnoreCase("admin")) {
                res.sendRedirect(req.getContextPath() + "/Error");
            } else {
                chain.doFilter(request, response);
            }
        }
    }
}
