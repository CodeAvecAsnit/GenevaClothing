package com.ecomm.np.genevaecommerce.Controllers;

import com.ecomm.np.genevaecommerce.DTO.AddressDTO;
import com.ecomm.np.genevaecommerce.DTO.OrderDTO;
import com.ecomm.np.genevaecommerce.Models.OrderedItems;
import com.ecomm.np.genevaecommerce.Security.CustomUser;
import com.ecomm.np.genevaecommerce.Services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PutMapping
    public ResponseEntity<OrderedItems> orderItem(@RequestBody OrderDTO orderDTO){
        OrderedItems items = orderService.orderItem(orderDTO);
        return ResponseEntity.ok(items);
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
}
