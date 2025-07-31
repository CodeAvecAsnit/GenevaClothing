package com.ecomm.np.genevaecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@SpringBootTest
public class Services {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void testMail(){
//        redisTemplate.opsForValue().set("email","whatmail@gmail.com");
        Object email = redisTemplate.opsForValue().get("email");
        int a = 1;
    }
}
