package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.extra.exception.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.service.application.GeneralItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    private final GeneralItemService generalItemService;

    public ItemController(@Qualifier("generalItemServiceImpl") GeneralItemService generalItemService) {
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
        int pageSize = 12;
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

}
