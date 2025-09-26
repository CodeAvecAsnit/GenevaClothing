package com.ecomm.np.genevaecommerce.service.infrastructure.impl;

import com.ecomm.np.genevaecommerce.model.dto.OrderData;
import com.ecomm.np.genevaecommerce.extra.util.NetworkUtils;
import com.ecomm.np.genevaecommerce.extra.util.UrlQrCode;
import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;
import com.ecomm.np.genevaecommerce.service.infrastructure.EncryptionService;
import com.ecomm.np.genevaecommerce.service.infrastructure.PackedNotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@EnableAsync
@Service
public class MailNotificationServiceImpl implements PackedNotificationService {

    private final JavaMailSender javaMailSender;
    private final EncryptionService encryptionService;


    @Autowired
    public MailNotificationServiceImpl(JavaMailSender javaMailSender,
                                       @Qualifier("encryptionServiceImpl") EncryptionService encryptionService) {
        this.javaMailSender = javaMailSender;
        this.encryptionService = encryptionService;
    }

    @Override
    @Async
    public void sendPackedNotice(String email, OrderedItems orderedItems) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
            helper.setTo(email);
            helper.setSubject("Order Packed Notice - Order #" + orderedItems.getoId());
            helper.setFrom("furnituremandu@gmail.com");
            StringBuilder stringBuilder = new StringBuilder("<p>This is a notice to let you know your order has been packed</p><br>");
            OrderData od = OrderData.buildFromOrderedItems(orderedItems);
            try {
                String Url = BuildURL(encryptionService.generateOrderData(od));
                stringBuilder.append("<a href=");
                stringBuilder.append(BuildForRedirection(orderedItems.getoId()));
                stringBuilder.append(">Click here</a>");
                ByteArrayDataSource dataSource = UrlQrCode.generateQR(Url);
                helper.addAttachment("qrcode.png",dataSource);
            }catch (Exception ex){
                throw new RuntimeException("Failed to send mail");
            }finally {
                String content = stringBuilder.toString();
                helper.setText(content,true);
                javaMailSender.send(mimeMessage);
            }
        }catch (MessagingException e){
            throw new RuntimeException("Failed to send Email");
        }
    }

    private String BuildURL(String param)throws Exception{
        return "http://" + NetworkUtils.getLocalIp() +
                ":8080/api/v1/get/order?textVal=" +
                URLEncoder.encode(param, StandardCharsets.UTF_8);
    }

    private String BuildForRedirection(int id)throws Exception{
        return "http://"+NetworkUtils.getLocalIp()+":5500/orderview.html?id="+id;
    }

}
