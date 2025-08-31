package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.model.dto.CollectionAndItemsDTO;
import com.ecomm.np.genevaecommerce.model.entity.BestCollection;
import com.ecomm.np.genevaecommerce.model.entity.Collection;
import com.ecomm.np.genevaecommerce.security.CustomUser;
import com.ecomm.np.genevaecommerce.service.application.BasicService;
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

    private final BasicService basicService;

    @Autowired
    public CollectionsController(BasicService basicService) {
        this.basicService = basicService;
    }

    @GetMapping
    public ResponseEntity<CollectionAndItemsDTO> provideCollection() {//in Use
        Collection collection = basicService.saveCollection();//migrate somewhere else
        return ResponseEntity.ok(CollectionAndItemsDTO.buildFromCollection(collection));
    }

    @GetMapping("/best")
    public ResponseEntity<BestCollection> getBest() {// in use
        try {
            return ResponseEntity.ok(basicService.bestCollection());
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }


    @PostMapping("/post/best")
    public ResponseEntity<String> updateBest(@RequestBody BestCollection collection) {
        try {
            return ResponseEntity.ok(basicService.updateBestCollection(collection));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Cannot update");
        }
    }

    @GetMapping("/check")// in use marked
    public ResponseEntity<Boolean> responseEntity(@AuthenticationPrincipal CustomUser customUser){
        Boolean isAdmin = customUser.getAuthorities().stream().anyMatch(auth->auth.getAuthority().equals("ADMIN"));
        return ResponseEntity.ok(isAdmin);
    }
}
