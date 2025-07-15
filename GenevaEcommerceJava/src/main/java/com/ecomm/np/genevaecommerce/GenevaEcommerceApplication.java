package com.ecomm.np.genevaecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

@SpringBootApplication
public class GenevaEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenevaEcommerceApplication.class, args);
	}


	@Bean
	public Random random(){
		return new Random();
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

}
