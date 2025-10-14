package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.service.infrastructure.EncryptionService;
import com.ecomm.np.genevaecommerce.service.infrastructure.impl.EncryptionServiceImpl;
import com.ecomm.np.genevaecommerce.model.dto.OrderData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author : Asnit Bakhati
 */

@RestController
@RequestMapping("api/v1/get/order")
public class QrController {
    private final EncryptionService encryptionService;

    @Autowired
    public QrController(@Qualifier("encryptionServiceImpl") EncryptionServiceImpl encryptionService) {
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public ResponseEntity<OrderData> getDecryptedData(@RequestParam String textVal) {
        try {
            return ResponseEntity.ok(encryptionService.verifyAndDecode(textVal));
        }catch (RuntimeException ex){
            return ResponseEntity.badRequest().build();
    }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }
}
