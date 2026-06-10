package com.ecomm.np.genevaecommerce.security;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Asnit Bakhati
 */

@Configuration
public class ImageConfig {

    @Value("${cloudinary.cloud.name}")
    private String cloudName;

    @Value("${cloudinary.cloud.apikey}")
    private String cloudKey;

    @Value("${cloudinary.cloud.apisecret}")
    private String cloudSecret;
    @Bean
    public Cloudinary getCloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", cloudKey,
                "api_secret",cloudSecret,
                "secure", true));
    }
}
