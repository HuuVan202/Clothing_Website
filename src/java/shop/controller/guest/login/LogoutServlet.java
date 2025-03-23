package shop.controller.guest.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Xóa session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Xóa tất cả cookie trừ username
        Cookie[] cookies = request.getCookies();
        boolean rememberMe = false;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("reMem") && cookie.getValue().equals("checked")) {
                    rememberMe = true;
                }
            }

            for (Cookie cookie : cookies) {
                if (!cookie.getName().equals("cUserName") && !(rememberMe && cookie.getName().equals("reMem"))) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    cookie.setPath("/"); // Đảm bảo bị xóa trên toàn hệ thống
                    response.addCookie(cookie);
                }
            }
        }

        // Chuyển hướng về trang chủ
        response.sendRedirect("home");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
