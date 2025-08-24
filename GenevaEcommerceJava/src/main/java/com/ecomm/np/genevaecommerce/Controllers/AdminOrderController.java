package com.ecomm.np.genevaecommerce.Controllers;


import com.ecomm.np.genevaecommerce.DTO.BasicDT0;
import com.ecomm.np.genevaecommerce.DTO.HistoryDTO;
import com.ecomm.np.genevaecommerce.DTO.OrderDataDTO;
import com.ecomm.np.genevaecommerce.Extras.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.services.OrderHistoryService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public AdminOrderController(OrderHistoryService orderHistoryService){
        this.orderHistoryService = orderHistoryService;
    }

    @GetMapping
    public String verifyAdmin(){
        return "Success";
    }

    @GetMapping("/orders/history")
    public ResponseEntity<Page<HistoryDTO>> getHistoryForADMIN(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "false")boolean asc,
                                                               @RequestParam(defaultValue="false") boolean active)
    {
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
    public ResponseEntity<OrderDataDTO> getOrderDataAdmin(@PathVariable int id){
        try{
            return ResponseEntity.ok(orderHistoryService.findOrderDataAdmin(id));
        }catch (ResourceNotFoundException rEx){
            return ResponseEntity.notFound().build();
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/pack/item/{tracer}")
    public ResponseEntity<BasicDT0> setOrderItemPacked(@PathVariable int tracer){
        try{
           if( orderHistoryService.setPackedByAdmin(tracer)){
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
            if( orderHistoryService.setDeliveredAdmin(order)){
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
            if( orderHistoryService.setAllPackedAdmin(order_id)){
                return ResponseEntity.ok(new BasicDT0("Order has been packed"));
            }else return ResponseEntity.badRequest().build();
        }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }
}
