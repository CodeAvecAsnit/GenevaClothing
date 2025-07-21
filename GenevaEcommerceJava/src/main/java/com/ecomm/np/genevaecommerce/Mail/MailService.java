package com.ecomm.np.genevaecommerce.Mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class MailService {
    private final Logger logger = LoggerFactory.getLogger(MailService.class);

    private final SecureRandom secureRandom;

    private final JavaMailSender javaMailSender;


    public MailService(JavaMailSender javaMailSender, SecureRandom secureRandom) {
        this.javaMailSender = javaMailSender;
        this.secureRandom = secureRandom;
    }

    public int generateAndSend(String email){
        int code = secureRandom.nextInt(100000,1000000);
        sendVerificationCode(email,code);
        return code;
    }



    @Async
    protected void sendVerificationCode(String email,int code){
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
    public void sendPromotionalEmail(List<String> emails,String title,String body) {
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
}
