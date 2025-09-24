package com.ecomm.np.genevaecommerce.TestPackage.Encryption;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private final OrderDataUtils orderDataUtils;
    private final AESEncrypt aesEncrypt;
    private final ObjectMapper objectMapper;

    @Autowired
    public EncryptionService(OrderDataUtils orderDataUtils, AESEncrypt aesEncrypt, ObjectMapper objectMapper) {
        this.orderDataUtils = orderDataUtils;
        this.aesEncrypt = aesEncrypt;
        this.objectMapper = objectMapper;
    }

    public String generateOrderData(OrderData orderData) throws Exception{
         orderDataUtils.loadThePayload(orderData);
         return aesEncrypt.encrypt(orderData.allToString());
    }

    public OrderData verifyAndDecode(String encryptedJson)throws RuntimeException,Exception{
        String decryptedJson = aesEncrypt.decrypt(encryptedJson);
        System.out.println(decryptedJson);
        OrderData orderData = objectMapper.readValue(decryptedJson,OrderData.class);
        System.out.println(orderData.toString());
        if(orderDataUtils.verifyObject(orderData)){
            return orderData;
        }else{
            throw new RuntimeException("Sorry but there were some changes made");
        }
    }
}
