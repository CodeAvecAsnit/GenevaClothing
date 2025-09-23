package com.ecomm.np.genevaecommerce;

import com.ecomm.np.genevaecommerce.extra.UrlQrCode;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
@EnableJpaAuditing
public class GenevaEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenevaEcommerceApplication.class, args);
	}

	@Bean
	public ApplicationRunner applicationRunner() {
		return args -> {
			UrlQrCode qrCode = new UrlQrCode();
			qrCode.generateQR();
			System.out.println("QR Code generated before server is ready to handle requests.");
		};
	}
}
