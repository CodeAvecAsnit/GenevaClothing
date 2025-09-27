package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.model.dto.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.model.dto.WishListDTO;
import com.ecomm.np.genevaecommerce.Security.CustomUser;
import com.ecomm.np.genevaecommerce.service.application.UserCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/user")
public class UsersController {

    private static final Logger log = LoggerFactory.getLogger(UsersController.class);
    private final UserCartService userCartService;

    @Autowired
    public UsersController(@Qualifier("userCartServiceImpl") UserCartService userCartService) {
        this.userCartService = userCartService;
    }


    @GetMapping("/cart/items")//in use.
    public ResponseEntity<Set<ItemDisplayDTO>> displayCart(@AuthenticationPrincipal CustomUser customUser) {
        if(customUser==null){
            return ResponseEntity.status(401).build();
        }
        try {
            Set<ItemDisplayDTO> cartItems = userCartService.getCartItems(customUser.getId());
            return ResponseEntity.ok(cartItems);
        } catch (UsernameNotFoundException ex) {
            log.warn("User not found. User ID: {}", customUser.getId(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            log.error("Unexpected error while retrieving cart items for User ID: {}", customUser.getId(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/wishlist-page/get-all")//
    public ResponseEntity<Set<WishListDTO>> getWishList(@AuthenticationPrincipal CustomUser customUser){
        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try{
            return ResponseEntity.ok(userCartService.getWishListFromUser(customUser.getId()));
        }
        catch (UsernameNotFoundException ex){
            return ResponseEntity.notFound().build();
        }catch (Exception except){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> responseEntity(@AuthenticationPrincipal CustomUser customUser){
        Boolean isAdmin = customUser.getAuthorities().stream().anyMatch(auth->auth.getAuthority().equals("ADMIN"));
        return ResponseEntity.ok(isAdmin);
    }
}
