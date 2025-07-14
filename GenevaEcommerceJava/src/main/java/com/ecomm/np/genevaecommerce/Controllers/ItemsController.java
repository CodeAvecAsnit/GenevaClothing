package com.ecomm.np.genevaecommerce.Controllers;

import com.ecomm.np.genevaecommerce.DTO.ListItemDTO;
import com.ecomm.np.genevaecommerce.Models.Items;
import com.ecomm.np.genevaecommerce.Services.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/items")
public class ItemsController {

    @Autowired
    private ItemsService itemsService;


    //too lazy to do this via bean
    @PostMapping
    public String saveGenders(){
        itemsService.saveGender();
        return "Success";
    }

    @PostMapping("/post")
    public ResponseEntity<Items> postItem(@RequestBody ListItemDTO itemDTO){
        return ResponseEntity.ok(itemsService.SaveItem(itemDTO));
    }
}
