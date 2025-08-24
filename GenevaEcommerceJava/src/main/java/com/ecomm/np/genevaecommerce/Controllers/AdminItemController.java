package com.ecomm.np.genevaecommerce.Controllers;


import com.ecomm.np.genevaecommerce.DTO.ListItemDTO;
import com.ecomm.np.genevaecommerce.services.AdminItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/item/admin")
public class AdminItemController {

    private final AdminItemService adminItemService;

    @Autowired
    public AdminItemController(AdminItemService adminItemService) {
        this.adminItemService = adminItemService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @RequestBody ListItemDTO itemDTO ) {
        try{
            adminItemService.saveItem(itemDTO,file);
            return ResponseEntity.ok().build();
        } catch (RuntimeException rEx) {
            return ResponseEntity.badRequest().build();
        }catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
