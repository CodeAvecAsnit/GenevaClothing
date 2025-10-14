package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.extra.exception.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.dto.BasicDT0;
import com.ecomm.np.genevaecommerce.service.application.OrderProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Asnit Bakhati
 */

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminOrderProcessingController {
    private final OrderProcessingService orderProcessingService;

    @Autowired
    public AdminOrderProcessingController(@Qualifier("orderProcessingServiceImpl") OrderProcessingService
                                                      orderProcessingService) {
        this.orderProcessingService = orderProcessingService;
    }

    @PostMapping("/pack/item/{tracer}")
    public ResponseEntity<BasicDT0> setOrderItemPacked(@PathVariable int tracer){
        try{
            if( orderProcessingService.setPackedByAdmin(tracer)){
                return ResponseEntity.ok(new BasicDT0("Item has been packed"));
            }else return ResponseEntity.badRequest().build();
        }catch (ResourceNotFoundException rEx){
            return ResponseEntity.notFound().build();
        }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/deliver/order/{order}")
    public ResponseEntity<BasicDT0> setOrderDelivered(@PathVariable int order){
        try{
            if( orderProcessingService.setDeliveredAdmin(order)){
                return ResponseEntity.ok(new BasicDT0("Item has been delivered"));
            }else return ResponseEntity.badRequest().build();
        }catch (ResourceNotFoundException rEx){
            return ResponseEntity.notFound().build();
        }catch (RuntimeException rEr){
            return ResponseEntity.badRequest().body(new BasicDT0(rEr.getMessage()));
        }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/pack/order/{order_id}")
    public ResponseEntity<BasicDT0> setOrderPacked(@PathVariable int order_id){
        try{
            if( orderProcessingService.setAllPackedAdmin(order_id)){
                return ResponseEntity.ok(new BasicDT0("Order has been packed"));
            }else return ResponseEntity.badRequest().build();
        }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }
}
