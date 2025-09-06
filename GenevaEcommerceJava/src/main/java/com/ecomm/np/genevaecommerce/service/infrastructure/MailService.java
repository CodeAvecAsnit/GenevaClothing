package com.ecomm.np.genevaecommerce.service.infrastructure;
import org.springframework.scheduling.annotation.Async;
import java.util.List;

public interface MailService {
    void sendVerificationCode(String email,int code);
    void sendPromotion(List<String> emails, String title, String body);
    void sendOrderConfirmationNotice(String email);
}
