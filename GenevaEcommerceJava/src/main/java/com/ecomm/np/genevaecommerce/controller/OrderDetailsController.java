package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.model.dto.AddressDTO;
import com.ecomm.np.genevaecommerce.Security.CustomUser;
import com.ecomm.np.genevaecommerce.service.application.OrderDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderDetailsController {

    private final OrderDetailsService orderDetailsService;

    public OrderDetailsController(@Qualifier("orderDetailsServiceImpl") OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    @PutMapping("update")
    public ResponseEntity<AddressDTO> updateAddress(@RequestBody AddressDTO addressDTO, @AuthenticationPrincipal CustomUser customUser){
        AddressDTO add = orderDetailsService.addOrUpdateAddress(customUser.getId(),addressDTO);
        return (add==null)?
                ResponseEntity.notFound().build():
                ResponseEntity.ok(add);
    }

    @GetMapping("/get/order_details")
    public ResponseEntity<AddressDTO> loadUserAddress(@AuthenticationPrincipal CustomUser customUser){
        try {
            return ResponseEntity.ok(orderDetailsService.getAddress(customUser.getId()));
        }catch (NullPointerException ex){
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
