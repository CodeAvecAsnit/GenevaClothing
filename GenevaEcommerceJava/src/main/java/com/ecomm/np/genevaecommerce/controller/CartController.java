package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.model.dto.BasicDT0;
import com.ecomm.np.genevaecommerce.model.dto.Checkers;
import com.ecomm.np.genevaecommerce.security.CustomUser;
import com.ecomm.np.genevaecommerce.service.application.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * @author : Asnit Bakhati
 */

@RestController
@RequestMapping("api/v1/items")
public class CartController {

    private final static Logger logger = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;

    @Autowired
    public CartController(@Qualifier("cartServiceImpl") CartService cartService) {
        this.cartService = cartService;
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
    public ResponseEntity<Checkers> checkList(@PathVariable int code, @AuthenticationPrincipal CustomUser customUser){
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
