/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.controller.guest.resetPassword;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

/**
 *
 * @author Admin
 */
public class Mail {

    public String generateOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void sendEmail(String toEmail, String subject, String content) throws MessagingException {
        final String fromEmail = "donatellophan@gmail.com";
        final String password = "skuxbkmyshcrkphl";

        // Cấu hình SMTP của Gmail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Tạo Authenticator
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        // Tạo phiên làm việc
        Session session = Session.getInstance(props, auth);

        // Tạo tin nhắn email
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromEmail));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        msg.setSubject(subject);
        msg.setText(content);

        Transport.send(msg);
    }

    public static void main(String[] args) {
        Mail mail = new Mail();

        // Test generateOTP
        String otp = mail.generateOTP();
        System.out.println("Generated OTP: " + otp);

        // Test sendEmail
        String toEmail = "vanph202@gmail.com"; // Thay bằng email người nhận
        String subject = "Test Email";
        String content = "Your OTP is: " + otp;

        try {
            mail.sendEmail(toEmail, subject, content);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email.");
        }
    }

}
