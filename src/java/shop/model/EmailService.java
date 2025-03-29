package shop.model;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Admin
 */
public class EmailService {

    private static final String HOST = "smtp.gmail.com";
    private static final String USERNAME = "clothingshopswp@gmail.com";
    private static final String PASSWORD = "otrq hmyx peii osar";
    private static final String PORT = "587";

    public static void sendMultiProductPaymentConfirmationCOD(String toEmail, String customerName, String addressCus, BigDecimal totalAmount, List<CartItem> cartItems) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Confirm successful payment");

            StringBuilder tableRows = new StringBuilder();

            if (cartItems != null && !cartItems.isEmpty()) {
                for (CartItem item : cartItems) {
                    if (item.getProduct() != null) {
                        BigDecimal itemPrice = item.getProduct().getSalePrice();
                        BigDecimal subtotal = itemPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

                        tableRows.append("<tr>")
                                .append("<td style='padding: 10px;'>").append(item.getProduct().getPro_name()).append("</td>")
                                .append("<td style='padding: 10px;'>").append(item.getSize()).append("</td>")
                                .append("<td style='padding: 10px;'>").append(item.getQuantity()).append("</td>")
                                .append("<td style='padding: 10px;'>").append(String.format("%,.0f", subtotal)).append(" VND</td>")
                                .append("<td style='padding: 10px;'>").append("COD</td>")
                                .append("</tr>");
                    }
                }
            }

            String content = "<h3>Hello " + customerName + ",</h3>"
                    + "<p>Thank you for purchasing in our store.</p>"
                    + "<table border='1' style='border-collapse: collapse; width: 100%; text-align: left;'>"
                    + "<tr><th style='padding: 10px;'>Product</th><th style='padding: 10px;'>Size</th><th style='padding: 10px;'>Quantity</th><th style='padding: 10px;'>Price</th><th style='padding: 10px;'>Payment</th></tr>"
                    + tableRows.toString()
                    + "</table>"
                    + "<p><b>Delivery Address:</b> " + addressCus + "</p>"
                    + "<p><b>Total Amount:</b> " + String.format("%,.0f", totalAmount) + " VND</p>"
                    + "<p>We will process your order as soon as possible.</p>"
                    + "<h4>Thank you!</h4>";

            message.setContent(content, "text/html; charset=UTF-8");
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void sendMultiProductPaymentConfirmationVNPay(String toEmail, String customerName, String addressCus, BigDecimal totalAmount, List<CartItem> cartItems) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Confirm successful payment");

            StringBuilder tableRows = new StringBuilder();

            if (cartItems != null && !cartItems.isEmpty()) {
                for (CartItem item : cartItems) {
                    if (item.getProduct() != null) {
                        BigDecimal itemPrice = item.getProduct().getSalePrice();
                        BigDecimal subtotal = itemPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

                        tableRows.append("<tr>")
                                .append("<td style='padding: 10px;'>").append(item.getProduct().getPro_name()).append("</td>")
                                .append("<td style='padding: 10px;'>").append(item.getSize()).append("</td>")
                                .append("<td style='padding: 10px;'>").append(item.getQuantity()).append("</td>")
                                .append("<td style='padding: 10px;'>").append(String.format("%,.0f", subtotal)).append(" VND</td>")
                                .append("<td style='padding: 10px;'>").append("Paid money</td>")
                                .append("</tr>");
                    }
                }
            }

            String content = "<h3>Hello " + customerName + ",</h3>"
                    + "<p>Thank you for purchasing in our store.</p>"
                    + "<table border='1' style='border-collapse: collapse; width: 100%; text-align: left;'>"
                    + "<tr><th style='padding: 10px;'>Product</th><th style='padding: 10px;'>Size</th><th style='padding: 10px;'>Quantity</th><th style='padding: 10px;'>Price</th><th style='padding: 10px;'>Payment</th></tr>"
                    + tableRows.toString()
                    + "</table>"
                    + "<p><b>Delivery Address:</b> " + addressCus + "</p>"
                    + "<p><b>Total Amount:</b> " + String.format("%,.0f", totalAmount) + " VND</p>"
                    + "<p>We will process your order as soon as possible.</p>"
                    + "<h4>Thank you!</h4>";

            message.setContent(content, "text/html; charset=UTF-8");
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void sendPaymentConfirmation(String toEmail, String customerName, String addressCus, BigDecimal totalAmount, List<CartItem> cartItems) {
        sendMultiProductPaymentConfirmationCOD(toEmail, customerName, addressCus, totalAmount, cartItems);
        sendMultiProductPaymentConfirmationVNPay(toEmail, customerName, addressCus, totalAmount, cartItems);

    }
}


