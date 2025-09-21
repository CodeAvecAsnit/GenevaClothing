package com.ecomm.np.genevaecommerce.service.infrastructure.impl;

import com.ecomm.np.genevaecommerce.model.dto.DisplayItemsDTO;
import com.ecomm.np.genevaecommerce.model.dto.OrderDTO;
import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;
import com.ecomm.np.genevaecommerce.service.infrastructure.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MailServiceImpl implements MailService {

    private final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
    private final JavaMailSender javaMailSender;

    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendVerificationCode(String email,int code){
        SimpleMailMessage message = new SimpleMailMessage();
        try{
            message.setFrom("furnituremandu@gmail.com");
            message.setTo(email);
            message.setSubject("Verification Code for Geneva Clothing");
            String body = "The verification code is "+code+". Please do not share this code.";
            message.setText(body);
            javaMailSender.send(message);
        }catch (Exception ex){
            logger.error("Some Error occurred while sending the email "+ ex.getMessage());
        }
    }


    @Async
    @Override
    public void sendPromotion(List<String> emails, String title, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("furnituremandu@gmail.com");
        message.setSubject(title);
        message.setText(body);
        for(String email : emails) {
            message.setTo(email);
            try {
                javaMailSender.send(message);
            } catch (MailSendException ex) {
                logger.error("There was a problem sending mail" + ex.getMessage());
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
    }


    @Async
    @Override
    public void sendOrderConfirmationNotice(String email, OrderedItems orderedItems) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
            helper.setTo(email);
            helper.setSubject("Order Confirmation - Order #" + orderedItems.getoId());
            helper.setFrom("furnituremandu@gmail.com");
            OrderDTO orderDTO= OrderDTO.buildFromOrderItems(orderedItems);
            String htmlContent = buildOrderConfirmationHtml(orderDTO);
            helper.setText(htmlContent,true);
            javaMailSender.send(mimeMessage);

        }catch (MessagingException e){
            throw new RuntimeException("Failed to send Email");
        }
    }

    private String buildOrderConfirmationHtml(OrderDTO orderDTO) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>")
                .append("<html lang='en'>")
                .append("<head>")
                .append("<meta charset='UTF-8'>")
                .append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
                .append("<title>Order Confirmation</title>")
                .append("<style>")
                .append("body { font-family: Arial, sans-serif; background-color: #ffffff; margin: 0; padding: 20px; color: #333333; }")
                .append(".container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden; }")
                .append(".header { background-color: #4CAF50; color: white; padding: 25px; text-align: center; }")
                .append(".header h1 { margin: 0; font-size: 24px; font-weight: normal; }")
                .append(".content { padding: 30px; }")
                .append(".order-info { background-color: #f9f9f9; padding: 20px; border-radius: 6px; margin-bottom: 25px; }")
                .append(".order-info h2 { margin-top: 0; color: #2e7d32; font-size: 18px; }")
                .append(".info-row { display: flex; justify-content: space-between; margin-bottom: 8px; }")
                .append(".info-label { font-weight: bold; color: #555; }")
                .append(".info-value { color: #333; }")
                .append(".items-section { margin-top: 25px; }")
                .append(".items-section h3 { color: #2e7d32; font-size: 18px; margin-bottom: 15px; }")
                .append(".items-table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }")
                .append(".items-table th { background-color: #f5f5f5; padding: 12px 8px; text-align: left; border-bottom: 2px solid #ddd; font-weight: bold; color: #555; }")
                .append(".items-table td { padding: 12px 8px; border-bottom: 1px solid #eee; }")
                .append(".item-image { width: 60px; height: 60px; object-fit: cover; border-radius: 4px; }")
                .append(".item-name { font-weight: bold; color: #333; }")
                .append(".price { font-weight: bold; color: #2e7d32; }")
                .append(".total-section { background-color: #f0f8f0; padding: 20px; border-radius: 6px; margin-top: 20px; }")
                .append(".total-row { display: flex; justify-content: space-between; margin-bottom: 8px; }")
                .append(".total-label { font-weight: bold; color: #555; }")
                .append(".total-value { font-weight: bold; color: #2e7d32; }")
                .append(".grand-total { border-top: 2px solid #4CAF50; padding-top: 10px; margin-top: 10px; font-size: 18px; }")
                .append(".footer { background-color: #f5f5f5; padding: 20px; text-align: center; color: #666; font-size: 14px; }")
                .append(".thank-you { text-align: center; color: #2e7d32; font-size: 16px; margin-bottom: 20px; }")
                .append("@media (max-width: 600px) {")
                .append("  .info-row { flex-direction: column; }")
                .append("  .total-row { flex-direction: column; }")
                .append("  .items-table th, .items-table td { padding: 8px 4px; font-size: 14px; }")
                .append("}")
                .append("</style>")
                .append("</head>")
                .append("<body>");

        html.append("<div class='container'>")
                .append("<div class='header'>")
                .append("<h1>Order Confirmation</h1>")
                .append("<p>Thank you for your order!</p>")
                .append("</div>");

        html.append("<div class='content'>")
                .append("<div class='thank-you'>")
                .append("<strong>Your order has been successfully placed and is being processed.</strong>")
                .append("</div>");

        html.append("<div class='order-info'>")
                .append("<h2>Order Details</h2>")
                .append("<div class='info-row'>")
                .append("<span class='info-label'>Order ID:</span>")
                .append("<span class='info-value'>#").append(orderDTO.getOrderId()).append("</span>")
                .append("</div>")
                .append("<div class='info-row'>")
                .append("<span class='info-label'>Order Date:</span>")
                .append("<span class='info-value'>").append(orderDTO.getOrderDate()).append("</span>")
                .append("</div>")
                .append("<div class='info-row'>")
                .append("<span class='info-label'>Number of Items:</span>")
                .append("<span class='info-value'>").append(orderDTO.getNoOfItems()).append("</span>")
                .append("</div>")
                .append("</div>");

        html.append("<div class='order-info'>")
                .append("<h2>Delivery Information</h2>")
                .append("<div class='info-row'>")
                .append("<span class='info-label'>Address:</span>")
                .append("<span class='info-value'>").append(orderDTO.getDeliveryAddress()).append("</span>")
                .append("</div>")
                .append("<div class='info-row'>")
                .append("<span class='info-label'>City:</span>")
                .append("<span class='info-value'>").append(orderDTO.getCity()).append("</span>")
                .append("</div>")
                .append("<div class='info-row'>")
                .append("<span class='info-label'>Province:</span>")
                .append("<span class='info-value'>").append(orderDTO.getProvince()).append("</span>")
                .append("</div>")
                .append("<div class='info-row'>")
                .append("<span class='info-label'>Phone Number:</span>")
                .append("<span class='info-value'>").append(orderDTO.getPhoneNumber()).append("</span>")
                .append("</div>")
                .append("</div>");

        html.append("<div class='items-section'>")
                .append("<h3>Ordered Items</h3>")
                .append("<table class='items-table'>")
                .append("<thead>")
                .append("<tr>")
                .append("<th>Item</th>")
                .append("<th>Name</th>")
                .append("<th>Size</th>")
                .append("<th>Price</th>")
                .append("<th>Qty</th>")
                .append("<th>Total</th>")
                .append("</tr>")
                .append("</thead>")
                .append("<tbody>");

        if (orderDTO.getImageDisplayList() != null) {
            for (DisplayItemsDTO item : orderDTO.getImageDisplayList()) {
                html.append("<tr>")
                        .append("<td>")
                        .append("<img src='").append(item.getImageLink()).append("' alt='").append(item.getItemName()).append("' class='item-image' />")
                        .append("</td>")
                        .append("<td class='item-name'>").append(item.getItemName()).append("</td>")
                        .append("<td>").append(item.getSize() != null ? item.getSize() : "N/A").append("</td>")
                        .append("<td class='price'>$").append(String.format("%.2f", item.getPrice())).append("</td>")
                        .append("<td>").append(item.getQuantity()).append("</td>")
                        .append("<td class='price'>$").append(String.format("%.2f", item.getTotalItemPrice())).append("</td>")
                        .append("</tr>");
            }
        }

        html.append("</tbody>")
                .append("</table>")
                .append("</div>");

        html.append("<div class='total-section'>")
                .append("<div class='total-row'>")
                .append("<span class='total-label'>Subtotal:</span>")
                .append("<span class='total-value'>$").append(orderDTO.getTotalPrice()).append("</span>")
                .append("</div>");

        if (orderDTO.getPaidPrice().compareTo(orderDTO.getTotalPrice()) != 0) {
            html.append("<div class='total-row'>")
                    .append("<span class='total-label'>Discount/Adjustment:</span>")
                    .append("<span class='total-value'>$")
                    .append(orderDTO.getTotalPrice().subtract(orderDTO.getPaidPrice()))
                    .append("</span>")
                    .append("</div>");
        }

        html.append("<div class='total-row grand-total'>")
                .append("<span class='total-label'>Total Paid:</span>")
                .append("<span class='total-value'>$").append(orderDTO.getPaidPrice()).append("</span>")
                .append("</div>")
                .append("</div>");
        html.append("</div>");

        html.append("<div class='footer'>")
                .append("<p>Thank you for shopping with us!</p>")
                .append("<p>If you have any questions about your order, please contact us at support@yourstore.com</p>")
                .append("<p style='font-size: 12px; margin-top: 15px;'>This is an automated email. Please do not reply to this email.</p>")
                .append("</div>");

        html.append("</div>")
                .append("</body>")
                .append("</html>");

        return html.toString();
    }
}
