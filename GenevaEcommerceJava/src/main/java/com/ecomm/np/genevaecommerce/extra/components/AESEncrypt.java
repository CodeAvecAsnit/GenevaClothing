package com.ecomm.np.genevaecommerce.extra.components;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AESEncrypt {

    private final String SECRET_KEY; // must be 16/24/32 chars
    private final String INIT_VECTOR; // must be 16 chars

    private SecretKeySpec spec;
    private IvParameterSpec iv;

    public AESEncrypt(@Value("${encryption.aes.key}") String secretKey,
                      @Value("${encryption.aes.initializer}") String initVector) {
        this.SECRET_KEY = secretKey;
        this.INIT_VECTOR = initVector;
    }

    @PostConstruct
    public void init() {
        this.iv = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
        this.spec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
    }

    public String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, spec, iv);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, spec, iv);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        String data = new String(decrypted, StandardCharsets.UTF_8);
        System.out.println(data);
        return data;
    }
}
