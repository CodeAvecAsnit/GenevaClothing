package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.dto.*;
import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.security.CustomUser;
import com.ecomm.np.genevaecommerce.service.CartService;
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


@RestController
@RequestMapping("/api/v1/items")
public class ItemsController {

    private final static Logger logger = LoggerFactory.getLogger(ItemsController.class);

    private final CartService cartService;


    @Autowired
    public ItemsController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/get/{id}")// in use
    public ResponseEntity<?> getItemDisplayById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(cartService.findById(id));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping
    public ResponseEntity<Page<?>> displayItems(@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String gender)  {//in use
        int pageSize = 8;
        Pageable pageable = PageRequest.of(page, pageSize);
        if (gender == null) {
            return ResponseEntity.ok(cartService.findAll(pageable));
        }
        try {
            return ResponseEntity.ok(cartService.findAll(pageable, gender));
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/get/latest")// in Use
    public ResponseEntity<List<ItemDisplayDTO>> latestItems() {
        return ResponseEntity.ok(cartService.displayNewArrivals());
    }


    @PutMapping("/cart/{code}")//in use
    public ResponseEntity<BasicDT0> addItemToCart(@AuthenticationPrincipal CustomUser customUser, @PathVariable int code) {
        BasicDT0 basicDT0 = new BasicDT0();
        try {
            basicDT0.setMessage(cartService.addItemToCart(customUser.getId(), code));
            return ResponseEntity.ok(basicDT0);
        } catch (IllegalAccessException ex) {
            basicDT0.setMessage("Item is already present");
            return ResponseEntity.status(409).body(basicDT0);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/cart-remove/{code}")//in use
    public ResponseEntity<BasicDT0> removeFromCart(@AuthenticationPrincipal CustomUser customUser, @PathVariable int code) {
    try{
        return ResponseEntity.ok(new BasicDT0(cartService.removeFromCart(customUser.getId(),code)));

    } catch (Exception e) {
        logger.warn(e.getMessage());
        return ResponseEntity.badRequest().body(new BasicDT0("Sorry requested resource was not found"));
    }
    }

    @DeleteMapping("/wishlist-remove/{code}")//in use
    public ResponseEntity<BasicDT0> removeFromWishList(@AuthenticationPrincipal CustomUser customUser, @PathVariable int code) {
        try{
            return ResponseEntity.ok(new BasicDT0(cartService.removeFromWishList(customUser.getId(),code)));

        } catch (Exception e) {
            logger.warn(e.getMessage());
            return ResponseEntity.badRequest().body(new BasicDT0("Sorry requested resource was not found"));
        }
    }

    @GetMapping("/check/{code}")//in use
    public ResponseEntity<Checkers> checkList(@PathVariable int code,@AuthenticationPrincipal CustomUser customUser){
        return ResponseEntity.ok(new Checkers(cartService.checkItemInCart(customUser.getId(),code),
                cartService.checkItemInWishList(customUser.getId(),code)));
    }

    @PutMapping("/wishlist/{code}")//in use
    public ResponseEntity<BasicDT0> addItemToWishList(@AuthenticationPrincipal CustomUser customUser,@PathVariable int code){
        BasicDT0 basicDT0 = new BasicDT0();
        try{
            basicDT0.setMessage(cartService.addItemToWishList(customUser.getId(),code));
            return ResponseEntity.ok(basicDT0);
        }catch (IllegalAccessException ex){
            basicDT0.setMessage("Item is already present");
            return ResponseEntity.status(409).body(basicDT0);
        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }
}
