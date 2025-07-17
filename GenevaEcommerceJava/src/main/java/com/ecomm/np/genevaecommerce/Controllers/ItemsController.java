package com.ecomm.np.genevaecommerce.Controllers;

import com.ecomm.np.genevaecommerce.DTO.CollectionDTO;
import com.ecomm.np.genevaecommerce.DTO.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.DTO.ListItemDTO;
import com.ecomm.np.genevaecommerce.DTO.NewCollectionDTO;
import com.ecomm.np.genevaecommerce.Models.Items;
import com.ecomm.np.genevaecommerce.Services.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/items")
public class ItemsController {

    @Autowired
    private ItemsService itemsService;


    @GetMapping("/new_collection")
    public ResponseEntity<List<NewCollectionDTO>> getNewCollection(){
        return ResponseEntity.ok(itemsService.findNewCollection());
    }


    @PostMapping("/post")
    public ResponseEntity<Items> postItem(@RequestBody ListItemDTO itemDTO){
        return ResponseEntity.ok(itemsService.SaveItem(itemDTO));
    }


    @GetMapping("/get/gender/{gen}")
    public ResponseEntity<List<ItemDisplayDTO>> displayByGender(@PathVariable String gen){
        return ResponseEntity.ok(itemsService.displayItems(gen));
    }


    @GetMapping("/get/latest")
    public ResponseEntity<List<ItemDisplayDTO>> latestItems(){
        return ResponseEntity.ok(itemsService.displayNewArrivals());
    }

    @GetMapping("/latest/collection")
    public ResponseEntity<List<CollectionDTO>> getLatestCollection(){
        return ResponseEntity.ok(null);
    }
}
