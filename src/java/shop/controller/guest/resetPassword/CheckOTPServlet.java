package shop.controller.guest.resetPassword;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CheckOTPServlet", urlPatterns = {"/CheckOTP"})
public class CheckOTPServlet extends HttpServlet {

    private final String PATH = "jsp/guest/checkOTP.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = (String) request.getSession().getAttribute("mail");
        System.out.println(email);
        if (email == null || email.isEmpty()) {
            request.setAttribute("message", "Email not found in session. Please try again.");
            request.getRequestDispatcher(PATH).forward(request, response);
            return;
        }

        Mail lib = new Mail();
        String otp = lib.generateOTP(); // Generate OTP

        try {
            // Send OTP email
            String subject = "Password Reset Request";
            String body = String.format(
                    "We received a request to reset your password.\n\n"
                            + "Your OTP code is: %s\n\n"
                            + "If you did not request this, please ignore this email.",
                    otp);

            lib.sendEmail(email, subject, body);
            
            request.getSession().setAttribute("otp", otp);
            request.setAttribute("message", "A verification code has been sent to your email.");
        } catch (MessagingException e) {
            e.printStackTrace();
            request.setAttribute("message", "Failed to send email. Please try again.");
        }

        request.getRequestDispatcher(PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String enteredOTP = request.getParameter("otp");
        String generatedOTP = (String) request.getSession().getAttribute("otp");

        if (generatedOTP == null || enteredOTP == null || !enteredOTP.equals(generatedOTP)) {
            request.setAttribute("mess", "Invalid OTP. Please try again.");
            request.getRequestDispatcher(PATH).forward(request, response);
            return;
        }

        // Xóa OTP khỏi session sau khi xác nhận
        request.getSession().removeAttribute("otp");

        // Chuyển hướng đến trang đặt lại mật khẩu
        response.sendRedirect("ResetPassword");
    }
}
