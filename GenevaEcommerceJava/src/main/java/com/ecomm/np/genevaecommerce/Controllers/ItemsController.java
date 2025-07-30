package com.ecomm.np.genevaecommerce.Controllers;

import com.ecomm.np.genevaecommerce.DTO.CollectionDTO;
import com.ecomm.np.genevaecommerce.DTO.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.DTO.ListItemDTO;
import com.ecomm.np.genevaecommerce.DTO.NewCollectionDTO;
import com.ecomm.np.genevaecommerce.Models.Items;
import com.ecomm.np.genevaecommerce.Services.ItemsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/items")
//@PreAuthorize("hasAuthority('ADMIN')")
public class ItemsController {

    private static Logger logger = LoggerFactory.getLogger(ItemsController.class);

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



    @GetMapping
    public ResponseEntity<Page<ItemDisplayDTO>> displayItems(@RequestParam(defaultValue = "0")int page,@RequestParam(required = false) String gender) throws Exception {
        int pageSize = 8;
        Pageable pageable = PageRequest.of(page,pageSize);
        if(gender==null){
            return ResponseEntity.ok(itemsService.findAll(pageable));
        }
        try {
        return ResponseEntity.ok( itemsService.findAll(pageable,gender));
    } catch (Exception ex) {
            logger.warn(ex.getMessage());
            return ResponseEntity.badRequest().build();
        }}



    @GetMapping("/get/latest")
    public ResponseEntity<List<ItemDisplayDTO>> latestItems(){
        return ResponseEntity.ok(itemsService.displayNewArrivals());
    }

    @GetMapping("/latest/collection")
    public ResponseEntity<List<CollectionDTO>> getLatestCollection(){
        return ResponseEntity.ok(null);
    }
}
