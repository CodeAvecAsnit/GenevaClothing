package com.ecomm.np.genevaecommerce.extra.util;

import com.ecomm.np.genevaecommerce.model.dto.OrderData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author : Asnit Bakhati
 */

@Component
public class OrderDataUtils {

    private final String secret;

    public OrderDataUtils(@Value("${hashing.hmac.secret}") String secret) {
        this.secret = secret;
    }

    public void loadThePayload(OrderData orderData)throws Exception{
        String order = orderData.toString();
        String payload = getHmac(order);
        orderData.setPayload(payload);
    }

    public boolean verifyObject(OrderData orderData) throws Exception {
        String payload = orderData.getPayload();
        if (payload == null) throw new IllegalStateException("Payload is null");
        String objectJson = orderData.toString();
        String regeneratedPayload = getHmac(objectJson);
        return payload.equals(regeneratedPayload);
    }


    private String getHmac(String message) throws Exception {
        byte[] byteArr = secret.getBytes();
        System.out.println(byteArr);
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(keySpec);
        byte[] hmacBytes = mac.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

}
