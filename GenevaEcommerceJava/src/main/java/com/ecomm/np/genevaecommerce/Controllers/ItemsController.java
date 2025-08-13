package com.ecomm.np.genevaecommerce.Controllers;


import com.ecomm.np.genevaecommerce.DTO.*;
import com.ecomm.np.genevaecommerce.Models.Checkers;
import com.ecomm.np.genevaecommerce.Models.Items;
import com.ecomm.np.genevaecommerce.Security.CustomUser;
import com.ecomm.np.genevaecommerce.Services.ItemsService;
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes


@RestController
@RequestMapping("/api/v1/items")
//@PreAuthorize("hasAuthority('ADMIN')")
public class ItemsController {

    private static Logger logger = LoggerFactory.getLogger(ItemsController.class);

    @Autowired
    private ItemsService itemsService;

    @GetMapping("/get/{id}")
    public ResponseEntity<ItemDisplayDTO> getItemDisplayById(@PathVariable int id){
        try{
            return ResponseEntity.ok(itemsService.findById(id));
        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

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

    @PutMapping("/cart/{code}")
    public ResponseEntity<BasicDT0> addItemToCart(@AuthenticationPrincipal CustomUser customUser,@PathVariable int code){
        BasicDT0 basicDT0 = new BasicDT0();
        try{
            basicDT0.setMessage(itemsService.addItemToCart(customUser.getId(),code));
            return ResponseEntity.ok(basicDT0);
        }catch (IllegalAccessException ex){
            basicDT0.setMessage("Item is already present");
            return ResponseEntity.status(409).body(basicDT0);
        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/check/{code}")
    public ResponseEntity<Checkers> checkList(@PathVariable int code,@AuthenticationPrincipal CustomUser customUser){
        return ResponseEntity.ok(new Checkers(itemsService.checkItemInCart(customUser.getId(),code),
                itemsService.checkItemInWishList(customUser.getId(),code)));
    }

    @PutMapping("/wishlist/{code}")
    public ResponseEntity<BasicDT0> addItemToWishList(@AuthenticationPrincipal CustomUser customUser,@PathVariable int code){
        BasicDT0 basicDT0 = new BasicDT0();
        try{
            basicDT0.setMessage(itemsService.addItemToWishList(customUser.getId(),code));
            return ResponseEntity.ok(basicDT0);
        }catch (IllegalAccessException ex){
            basicDT0.setMessage("Item is already present");
            return ResponseEntity.status(409).body(basicDT0);
        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }
}
