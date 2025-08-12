package com.ecomm.np.genevaecommerce.Controllers;

import com.ecomm.np.genevaecommerce.DTO.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.DTO.UserDTO;
import com.ecomm.np.genevaecommerce.DTO.WishListDTO;
import com.ecomm.np.genevaecommerce.Models.UserModel;
import com.ecomm.np.genevaecommerce.Security.CustomUser;
import com.ecomm.np.genevaecommerce.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> responseEntity(@AuthenticationPrincipal CustomUser customUser){
        Boolean isAdmin = customUser.getAuthorities().stream().anyMatch(auth->auth.getAuthority().equals("ADMIN"));
        return ResponseEntity.ok(isAdmin);
    }

    @PostMapping("/post")
    public ResponseEntity<UserModel> putUser(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.saveUser(userDTO));
    }

    @PutMapping("/cart")
    public ResponseEntity<String> addToCart(@RequestParam int user_id,@RequestParam int item_id){
        try {
            return ResponseEntity.ok(userService.itemToCart(user_id, item_id));
        }catch (Exception ex){
            log.warn(ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/cart/items")
    public ResponseEntity<Set<ItemDisplayDTO>> displayCart(@AuthenticationPrincipal CustomUser customUser) {
        try {
            Set<ItemDisplayDTO> cartItems = userService.getCartItems(customUser.getId());
            return ResponseEntity.ok(cartItems);
        } catch (UsernameNotFoundException ex) {
            log.warn("User not found. User ID: {}", customUser.getId(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            log.error("Unexpected error while retrieving cart items for User ID: {}", customUser.getId(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/wishlist")
    public ResponseEntity<String> addToWishList(@RequestParam int user_id,@RequestParam int item_id){
        return ResponseEntity.ok(userService.itemToWishList(user_id,item_id));
    }

    @GetMapping("/wishlist/items")
    public ResponseEntity<Set<ItemDisplayDTO>> DisplayWishList(@AuthenticationPrincipal CustomUser customUser){
        try {
            Set<ItemDisplayDTO> cartItems = userService.getWishListItems(customUser.getId());
            return ResponseEntity.ok(cartItems);
        } catch (UsernameNotFoundException ex) {
            log.warn("User not found. User ID: {}", customUser.getId(), ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            log.error("Unexpected error while retrieving cart items for User ID: {}", customUser.getId(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/wishlist-page/get-all")
    public ResponseEntity<Set<WishListDTO>> getWishList(@AuthenticationPrincipal CustomUser customUser){
        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try{
            return ResponseEntity.ok(userService.getWishListFromUser(customUser.getId()));
        }
        catch (UsernameNotFoundException ex){
            return ResponseEntity.notFound().build();
        }catch (Exception except){
            return ResponseEntity.internalServerError().build();
        }
    }
}
