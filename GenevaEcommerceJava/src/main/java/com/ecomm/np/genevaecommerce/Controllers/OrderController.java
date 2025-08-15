package com.ecomm.np.genevaecommerce.Controllers;


import com.ecomm.np.genevaecommerce.DTO.*;
import com.ecomm.np.genevaecommerce.Extras.CodeErrorException;
import com.ecomm.np.genevaecommerce.Extras.OutOfStockException;
import com.ecomm.np.genevaecommerce.Extras.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.Models.OrderedItems;
import com.ecomm.np.genevaecommerce.Security.CustomUser;
import com.ecomm.np.genevaecommerce.Services.CheckoutService;
import com.ecomm.np.genevaecommerce.Services.OrderService;
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
    private final OrderService orderService;
    private final CheckoutService checkoutService;

    @Autowired
    public OrderController(OrderService orderService,CheckoutService checkoutService) {
        this.orderService = orderService;
        this.checkoutService = checkoutService;
    }

    @PutMapping
    public ResponseEntity<OrderedItems> orderItem(@RequestBody OrderDTO orderDTO){
        OrderedItems items = orderService.orderItem(orderDTO);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/fetch/order")
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


    @PutMapping("update")
    public ResponseEntity<AddressDTO> updateAddress(@RequestBody AddressDTO addressDTO,@AuthenticationPrincipal CustomUser customUser){
        AddressDTO add = orderService.addOrUpdateAddress(customUser.getId(),addressDTO);
        return (add==null)?
                ResponseEntity.notFound().build():
                ResponseEntity.ok(add);
    }

    @GetMapping("/get/order_details")
    public ResponseEntity<AddressDTO> loadUserAddress(@AuthenticationPrincipal CustomUser customUser){
        try {
            return ResponseEntity.ok(orderService.getAddress(customUser.getId()));
        }catch (NullPointerException ex){
            return ResponseEntity.status(404).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/cart/checkout/{id}")
    public String checkOutCart(@PathVariable int id){
        try{
            orderService.checkOutCart(id);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Success";
    }

    @PostMapping
    private ResponseEntity<BasicDT0> respondToCheckout(@RequestBody List<ItemQuantity> itemQuantityList,@AuthenticationPrincipal CustomUser customUser) {
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
    private ResponseEntity<BasicDT0> respondGivePrice(@RequestBody List<ItemQuantity> itemQuantityList) {
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


}
