package com.ecomm.np.genevaecommerce.Controllers;

import com.ecomm.np.genevaecommerce.DTO.CollectionAndItemsDTO;
import com.ecomm.np.genevaecommerce.Models.BestCollection;
import com.ecomm.np.genevaecommerce.Models.Collection;
import com.ecomm.np.genevaecommerce.Security.CustomUser;
import com.ecomm.np.genevaecommerce.Services.HomeService;
import com.ecomm.np.genevaecommerce.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;

@RestController
@RequestMapping("api/v1/home")
public class CollectionsController {


    private static final Logger log = LoggerFactory.getLogger(CollectionsController.class);

    private final HomeService homeService;
    private final UserService userService;

    @Autowired
    public CollectionsController(HomeService homeService, UserService userService) {
        this.homeService = homeService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<CollectionAndItemsDTO> provideCollection() {
        Collection collection = homeService.saveCollection();
        return ResponseEntity.ok(CollectionAndItemsDTO.buildFromCollection(collection));
    }

    @GetMapping("/best")
    public ResponseEntity<BestCollection> getBest() {
        try {
            return ResponseEntity.ok(homeService.bestCollection());
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }



    @PostMapping("/post/best")
    public ResponseEntity<String> updateBest(@RequestBody BestCollection collection) {
        try {
            return ResponseEntity.ok(homeService.updateBestCollection(collection));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Cannot update");
        }
    }

    @GetMapping("/cart/count")
    public ResponseEntity<Integer> findCartCount(@AuthenticationPrincipal CustomUser customUser){
        return ResponseEntity.ok(userService.findNoOfItemsInCart(customUser.getId()));
    }
}
