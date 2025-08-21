package com.ecomm.np.genevaecommerce.Controllers;

import com.ecomm.np.genevaecommerce.DTO.HistoryDTO;
import com.ecomm.np.genevaecommerce.DTO.OrderDataDTO;
import com.ecomm.np.genevaecommerce.Security.CustomUser;
import com.ecomm.np.genevaecommerce.services.OrderHistoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/past/orders")
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

    @GetMapping("/past/order/{id}")
    public ResponseEntity<OrderDataDTO> getOrderData(@PathVariable int id,@AuthenticationPrincipal CustomUser customUser) {
        try {
            return ResponseEntity.ok(orderHistoryService.findOrderData(id, customUser.getId()));
        }catch (AccessDeniedException aEx){
            return ResponseEntity.status(401).build();
    }catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }


   @GetMapping("/admin/orders")
   @PreAuthorize("hasAuthority('ADMIN')")
   public ResponseEntity<Page<HistoryDTO>> getHistoryForUser(@RequestParam(defaultValue = "0") int page,@AuthenticationPrincipal CustomUser customUser){
       int pageSize = 20;
       Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "orderInitiatedDate"));
       try{
           return ResponseEntity.ok(orderHistoryService.findAllPagesForAdmin(pageable));
       }catch (Exception ex){
           log.error(ex.getMessage());
           return  ResponseEntity.badRequest().build();
       }
   }
}
