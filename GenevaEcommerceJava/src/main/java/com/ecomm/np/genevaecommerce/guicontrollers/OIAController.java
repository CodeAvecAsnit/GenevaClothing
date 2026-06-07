package com.ecomm.np.genevaecommerce.guicontrollers;

import com.ecomm.np.genevaecommerce.service.modelservice.OrderItemAuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/gui")
public class OIAController {

    private final OrderItemAuditService orderItemAuditService;


    public OIAController(OrderItemAuditService orderItemAuditService) {
        this.orderItemAuditService = orderItemAuditService;
    }

    @PreAuthorize("hasAuthority('SYSTEM')")
    @GetMapping("/order-count")
    public ResponseEntity<Map<String,Integer>> findItemPacked(){
        Integer val = orderItemAuditService.findTotalItemsPackedCount(false);
        return ResponseEntity.ok(Map.of("Count",val));
    }
}
