package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.model.dto.ListItemDTO;
import com.ecomm.np.genevaecommerce.extra.exception.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.service.admin.AdminItemService;
import com.ecomm.np.genevaecommerce.service.admin.impl.AdminItemServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * @author : Asnit Bakhati
 */

@RestController
@RequestMapping("/api/v1/item/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminItemController {

    private final Logger logger = LoggerFactory.getLogger(AdminItemServiceImpl.class);
    private final AdminItemService adminItemService;

    @Autowired
    public AdminItemController(AdminItemServiceImpl adminItemServiceImpl) {
        this.adminItemService = adminItemServiceImpl;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("itemData") String itemDataJson) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is required");
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            ListItemDTO itemDTO = mapper.readValue(itemDataJson, ListItemDTO.class);
            adminItemService.saveItem(itemDTO, file);
            return ResponseEntity.ok().body("Item created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> readItemData(@PathVariable int id){
        try{
            return ResponseEntity.ok(adminItemService.readDataForAdmin(id));
        }catch (ResourceNotFoundException rEx){
            return ResponseEntity.badRequest().body(rEx.getMessage());
        }catch (Exception ex){
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateItem(@PathVariable int id,
                                        @RequestParam("file") MultipartFile file,
                                        @RequestParam("itemData") String dataJson){
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is required for updating");
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            ListItemDTO itemDTO = mapper.readValue(dataJson, ListItemDTO.class);
            adminItemService.updateItemByAdmin(itemDTO, file, id);
            return ResponseEntity.ok().body("Item created successfully");
        } catch (ResourceNotFoundException rEx){
            return ResponseEntity.notFound().build();
        }catch (IOException ex){
            return ResponseEntity.badRequest().body("Unable to uploadImage the file");
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable int id) {
        try {
            adminItemService.deleteItem(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex){
            return ResponseEntity.notFound().build();
    }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/collection")
    public ResponseEntity<?> addNewCollection(@RequestParam("name") String collectionName,
                                              @RequestParam("description") String collectionDescription){
        try{
            adminItemService.createNewCollection(collectionName,collectionDescription);
            return ResponseEntity.ok().build();
        }catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }
}
