package com.ecomm.np.genevaecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class GenevaEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenevaEcommerceApplication.class, args);
	}

}
