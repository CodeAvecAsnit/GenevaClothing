package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.model.dto.HistoryDTO;
import com.ecomm.np.genevaecommerce.model.dto.OrderDTO;
import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.service.application.OrderHistoryService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminOrderController {

    private final Logger logger = LoggerFactory.getLogger(AdminOrderController.class);

    private final OrderHistoryService orderHistoryService;

    @Autowired
    public AdminOrderController(@Qualifier("orderHistoryServiceImpl") OrderHistoryService orderHistoryService){
        this.orderHistoryService = orderHistoryService;
    }

    @GetMapping
    public String verifyAdmin(){
        return "Success";
    }

    @GetMapping("/orders/history")
    public ResponseEntity<Page<HistoryDTO>> getHistoryForADMIN(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "false")boolean asc,@RequestParam(defaultValue="false") boolean active) {
        int pageSize = 20;
        Sort sort = asc
                ? Sort.by(Sort.Direction.ASC, "orderInitiatedDate")
                : Sort.by(Sort.Direction.DESC, "orderInitiatedDate");
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        try{
            if(active) return ResponseEntity.ok(orderHistoryService.findAllPagesForAdmin(pageable, true));
            return ResponseEntity.ok(orderHistoryService.findAllPagesForAdmin(pageable));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            logger.error("Error fetching order history", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/orders/history/{id}")
    public ResponseEntity<OrderDTO> getOrderDataAdmin(@PathVariable int id){
        try{
            return ResponseEntity.ok(orderHistoryService.findOrderDataAdmin(id));
        }catch (ResourceNotFoundException rEx){
            return ResponseEntity.notFound().build();
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}