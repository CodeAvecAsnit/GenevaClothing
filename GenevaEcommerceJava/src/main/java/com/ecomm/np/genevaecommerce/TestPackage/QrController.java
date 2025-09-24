package com.ecomm.np.genevaecommerce.TestPackage;

import com.ecomm.np.genevaecommerce.TestPackage.Encryption.EncryptionService;
import com.ecomm.np.genevaecommerce.TestPackage.Encryption.OrderData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/get/order")
public class QrController {
    private final EncryptionService encryptionService;

    @Autowired
    public QrController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PostMapping
    public ResponseEntity<String> getEncryptedText() {
        OrderData orderData = new OrderData(1, 200, 100, 2, "Kathmandu Sahara");
        try {
            return ResponseEntity.ok(encryptionService.generateOrderData(orderData));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
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
