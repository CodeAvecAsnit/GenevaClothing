package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.dto.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.service.authservice.GeneralItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private static Logger logger = LoggerFactory.getLogger(ItemController.class);
    private final GeneralItemService generalItemService;

    public ItemController(GeneralItemService generalItemService) {
        this.generalItemService = generalItemService;
    }

    @GetMapping("/get/{id}")// in use
    public ResponseEntity<?> getItemDisplayById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(generalItemService.findById(id));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<?>> displayItems(@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String gender)  {//in use
        int pageSize = 8;
        Pageable pageable = PageRequest.of(page, pageSize);
        try {
            if (gender == null) {
                return ResponseEntity.ok(generalItemService.findAll(pageable));
            }else return ResponseEntity.ok(generalItemService.findAll(pageable, gender));
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get/latest")// in Use
    public ResponseEntity<List<ItemDisplayDTO>> latestItems() {
        return ResponseEntity.ok(generalItemService.displayNewArrivals());
    }
}
