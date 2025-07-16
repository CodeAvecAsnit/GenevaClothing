package com.ecomm.np.genevaecommerce.Controllers;

import com.ecomm.np.genevaecommerce.DTO.OrderDTO;
import com.ecomm.np.genevaecommerce.DTO.UpdateAdressDTO;
import com.ecomm.np.genevaecommerce.Models.OrderDetails;
import com.ecomm.np.genevaecommerce.Models.OrderedItems;
import com.ecomm.np.genevaecommerce.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PutMapping
    public ResponseEntity<OrderedItems> orderItem(@RequestBody OrderDTO orderDTO){
        OrderedItems items = orderService.orderItem(orderDTO);
        return (orderDTO==null)?
                ResponseEntity.badRequest().build():
                ResponseEntity.ok(items);
    }

    @PutMapping("/update")
    public ResponseEntity<OrderDetails> updateDeliveryAddress(@RequestBody UpdateAdressDTO body){
        OrderDetails or = orderService.updateAddress(body);
        return (or==null)?
                ResponseEntity.badRequest().build():
                ResponseEntity.ok(or);
    }
}
