package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.model.dto.*;
import com.ecomm.np.genevaecommerce.security.CustomUser;
import com.ecomm.np.genevaecommerce.service.application.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final CheckoutService checkoutService;


    @Autowired
    public OrderController(@Qualifier("checkoutServiceImpl") CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/checkout/register")
    public ResponseEntity<BasicDT0> registerOrder(@RequestBody CheckoutIncDTO dto, @AuthenticationPrincipal CustomUser customUser){
        try{
            if (checkoutService.checkoutOrder(dto,customUser.getId())) {
                return ResponseEntity.ok(new BasicDT0("Success"));
            }else return ResponseEntity.badRequest().build();
        }catch (Exception ex){
            return ResponseEntity.internalServerError().body(new BasicDT0("Something went wrong"));
        }
    }
}