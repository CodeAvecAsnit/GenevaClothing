package com.ecomm.np.genevaecommerce.Controllers;

import com.ecomm.np.genevaecommerce.DTO.UserDTO;
import com.ecomm.np.genevaecommerce.Models.Items;
import com.ecomm.np.genevaecommerce.Models.UserModel;
import com.ecomm.np.genevaecommerce.Security.CustomUser;
import com.ecomm.np.genevaecommerce.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/user")
public class UsersController {

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
        return ResponseEntity.ok(userService.itemToCart(user_id,item_id));
    }

    @GetMapping("/cart/items")
    public ResponseEntity<Set<Items>> DisplayCart(@RequestParam int user_id){
        return ResponseEntity.ok(userService.getCartItems(user_id));
    }

    @PutMapping("/wishlist")
    public ResponseEntity<String> addToWishList(@RequestParam int user_id,@RequestParam int item_id){
        return ResponseEntity.ok(userService.itemToWishList(user_id,item_id));
    }

    @GetMapping("/wishlist/items")
    public ResponseEntity<Set<Items>> DisplayWishList(@RequestParam int user_id){
        return ResponseEntity.ok(userService.getWishListItems(user_id));
    }
}
