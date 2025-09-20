package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.extra.UrlQrCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/qr")
public class DummyController {


    private static final Logger log = LoggerFactory.getLogger(DummyController.class);

    @PostMapping
    public ResponseEntity<?> generateQR(){
        try {
            UrlQrCode qrCode = new UrlQrCode();
            qrCode.generateQR();
            Path pathToImage = Paths.get("").toAbsolutePath().resolve("qrcode.png");
            Resource resource= new UrlResource(pathToImage.toUri());
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).build();
        } catch (Exception e) {
            System.err.println("QR Code generation failed:");
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }
}
