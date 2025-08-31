package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.dto.*;
import com.ecomm.np.genevaecommerce.extra.CodeErrorException;
import com.ecomm.np.genevaecommerce.extra.OutOfStockException;
import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.security.CustomUser;
import com.ecomm.np.genevaecommerce.service.CheckoutService;
import com.ecomm.np.genevaecommerce.service.OrderDetailsService;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderDetailsService orderDetailsService;
    private final CheckoutService checkoutService;

    @Autowired
    public OrderController(OrderDetailsService orderDetailsService, CheckoutService checkoutService) {
        this.orderDetailsService = orderDetailsService;
        this.checkoutService = checkoutService;
    }


    @GetMapping("/fetch/order")//in use
    public ResponseEntity<CheckDTO> displayOrderPage(@RequestParam int code,@AuthenticationPrincipal CustomUser user){
        try{
            return ResponseEntity.ok(checkoutService.fetchCheckPage(code,user.getId()));
        }catch (UsernameNotFoundException usEX){
            log.error(usEX.getMessage());
            return ResponseEntity.notFound().build();
        }catch(OutOfStockException ex){
            return ResponseEntity.status(408).build();
        }catch (CodeErrorException codeEx){
            return ResponseEntity.status(401).build();
        }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("update")// in use
    public ResponseEntity<AddressDTO> updateAddress(@RequestBody AddressDTO addressDTO,@AuthenticationPrincipal CustomUser customUser){
        AddressDTO add = orderDetailsService.addOrUpdateAddress(customUser.getId(),addressDTO);
        return (add==null)?
                ResponseEntity.notFound().build():
                ResponseEntity.ok(add);
    }

    @GetMapping("/get/order_details")//in use
    public ResponseEntity<AddressDTO> loadUserAddress(@AuthenticationPrincipal CustomUser customUser){
        try {
            return ResponseEntity.ok(orderDetailsService.getAddress(customUser.getId()));
        }catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(404).build();
        }
    }


    @PostMapping
    private ResponseEntity<BasicDT0> respondToCheckout(@RequestBody List<QuantityItemDTO> itemQuantityList, @AuthenticationPrincipal CustomUser customUser) {//in use
        try {
            int code = checkoutService.processAndSaveRequest(itemQuantityList);
            BasicDT0 basicDT0 = new BasicDT0();
            if (code == 0) {
                basicDT0.setMessage("Item cannot be processed.Quantity is higher than available stock");
                return ResponseEntity.badRequest().body(basicDT0);
            }
            basicDT0.setMessage(String.valueOf(code + customUser.getId()));
            return ResponseEntity.ok(basicDT0);
        }catch (OutOfStockException outOfStock){
            return ResponseEntity.status(409).body(new BasicDT0(outOfStock.getMessage()));
    }   catch (ResourceNotFoundException rEx){
            log.warn(rEx.getMessage());
            return ResponseEntity.badRequest().body(new BasicDT0("Please try refreshing the page and try again"));
        }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/checkout/price")
    public ResponseEntity<BasicDT0> respondGivePrice(@RequestBody List<QuantityItemDTO> itemQuantityList) {
        try {
            float code = checkoutService.liveCalculator(itemQuantityList);
            BasicDT0 basicDT0 = new BasicDT0();
            if (code == 0) {
                basicDT0.setMessage("Items cannot be processed.Quantity is higher than available stock");
                return ResponseEntity.badRequest().body(basicDT0);
            }
            basicDT0.setMessage(String.format("%.0f",code));
            return ResponseEntity.ok(basicDT0);
        }catch (ResourceNotFoundException rEx){
            log.warn(rEx.getMessage());
            return ResponseEntity.badRequest().body(new BasicDT0("Please try refreshing the page and try again"));
        }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/checkout/item")//in use
    public ResponseEntity<BasicDT0> orderSingleItem(
            @RequestParam int id, @RequestParam int quantity, @NotBlank@RequestParam String size,
            @AuthenticationPrincipal CustomUser customUser)
    {
        try{
            int code = checkoutService.processSingleItem(id,quantity,size);
            code+=customUser.getId();
            return ResponseEntity.ok(new BasicDT0(String.valueOf(code)));
        }catch (ResourceNotFoundException rEx){
            return ResponseEntity.status(404).body(new BasicDT0(rEx.getMessage()));
        }catch (OutOfStockException oEx){
            log.warn("Item with id : "+id+" is out of stock");
            return ResponseEntity.status(409).body(new BasicDT0(oEx.getMessage()));
        }catch (Exception ex){
            log.error(ex.getMessage());
            return ResponseEntity.internalServerError().body(new BasicDT0("Some Internal Error occurred."));
        }
    }

    @PostMapping("/checkout/register")//in use
    public ResponseEntity<BasicDT0> registerOrder(@RequestBody CheckoutIncDTO dto,@AuthenticationPrincipal CustomUser customUser){
        try{
            if (checkoutService.checkoutOrder(dto,customUser.getId())) {
                return ResponseEntity.ok(new BasicDT0("Success"));
            }else return ResponseEntity.badRequest().build();
        }catch (Exception ex){
            return ResponseEntity.internalServerError().body(new BasicDT0("Something went wrong"));
        }
    }
}
