package com.ecomm.np.genevaecommerce;

import com.ecomm.np.genevaecommerce.service.authservice.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DummyLogin {

    @Autowired
    private AuthService authService;

    public static void main(String[] args) {
        String testUserEmail ="aranzabakhati@gmail.com";
        String password = "aranza123";
        try{
            authService
        }
    }
}
