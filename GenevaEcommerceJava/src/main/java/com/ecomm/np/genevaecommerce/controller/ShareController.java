package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.extra.components.QRComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/share/qr")
public class ShareController {

    private final QRComponent qrComponent;

    @Autowired
    public ShareController(QRComponent qrComponent) {
        this.qrComponent = qrComponent;
    }

    @GetMapping
    public ResponseEntity<byte[]> sendQR(){
        try{
            byte[] qrArray = qrComponent.getQrByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return ResponseEntity.ok().headers(headers).body(qrArray);
        }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }
}
