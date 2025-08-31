package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.model.dto.HistoryDTO;
import com.ecomm.np.genevaecommerce.model.dto.OrderDTO;
import com.ecomm.np.genevaecommerce.security.CustomUser;
import com.ecomm.np.genevaecommerce.service.application.OrderHistoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/history")
public class OrderHistoryController {

    private static final Logger log = LogManager.getLogger(OrderHistoryController.class);
    private final OrderHistoryService orderHistoryService;

    @Autowired
    public OrderHistoryController(OrderHistoryService orderHistoryService){
        this.orderHistoryService=orderHistoryService;
    }

    @GetMapping("/past/orders")//in use
    public ResponseEntity<Page<HistoryDTO>> getPageHistory(@AuthenticationPrincipal CustomUser customUser,@RequestParam(defaultValue = "0") int page){
        int pageSize = 8;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "orderInitiatedDate"));
        try{
            return ResponseEntity.ok(orderHistoryService.findUserHistory(customUser.getId(),pageable));
        }catch (Exception ex){
            log.error(ex.getMessage());
            return  ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/past/order/{id}")//in use
    public ResponseEntity<OrderDTO> getOrderData(@PathVariable int id, @AuthenticationPrincipal CustomUser customUser) {
        try {
            return ResponseEntity.ok(orderHistoryService.findOrderData(id, customUser.getId()));
        }catch (AccessDeniedException aEx){
            return ResponseEntity.status(401).build();
    }catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }

}
