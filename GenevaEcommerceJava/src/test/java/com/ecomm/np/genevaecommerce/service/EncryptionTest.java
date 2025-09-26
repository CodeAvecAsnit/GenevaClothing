package com.ecomm.np.genevaecommerce.service;

import com.ecomm.np.genevaecommerce.extra.components.AESEncrypt;
import com.ecomm.np.genevaecommerce.model.dto.OrderData;
import com.ecomm.np.genevaecommerce.extra.util.OrderDataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptionTest {

    private AESEncrypt aesEncrypt;
    private OrderDataUtils orderDataUtils;

    private Logger logger= LoggerFactory.getLogger(EncryptionTest.class);

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
         String secretKey = "HelloWorld120967";
        String initVector = "RandomVectorInit";
        aesEncrypt = new AESEncrypt(secretKey, initVector);
        aesEncrypt.init();

        orderDataUtils = new OrderDataUtils("yopierreyouwannacomeout7hereinnewyorkmagic3johnsonianiamtrynahit");
    }

    @Test
    public void testEncryptionAndDecryption() {
        try {
            OrderData orderData = new OrderData(1, 200, 100, 2, "Kathmandu Sahara");
            orderDataUtils.loadThePayload(orderData);
            String plainText = orderData.allToString();
            System.out.println(plainText);
            String cipherText = aesEncrypt.encrypt(plainText);
            System.out.println(cipherText);
            String decryptedText = aesEncrypt.decrypt(cipherText);
            System.out.println(decryptedText);
            assert plainText.equals(decryptedText);
            ObjectMapper mapper = new ObjectMapper();
            OrderData orderData1 = mapper.readValue(decryptedText,OrderData.class);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

}
