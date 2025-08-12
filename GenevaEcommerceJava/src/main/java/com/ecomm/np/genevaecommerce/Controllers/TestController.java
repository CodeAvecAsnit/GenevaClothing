package com.ecomm.np.genevaecommerce.Controllers;

import com.ecomm.np.genevaecommerce.DTO.WishListDTO;
import com.ecomm.np.genevaecommerce.Security.CustomUser;
import com.ecomm.np.genevaecommerce.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api/v1/wishlist/user")
public class TestController {

    private final UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get/test")
    public String testing(){
        return "Test runs";
    }

    @GetMapping("/get/auth")
    public String testing(@AuthenticationPrincipal CustomUser customUser){
        return customUser.getEmail();
    }

    @GetMapping("get/page")
    public ResponseEntity<Set<WishListDTO>> getWishList(@AuthenticationPrincipal CustomUser customUser){
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
