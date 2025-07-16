package com.ecomm.np.genevaecommerce.Controllers;


import com.ecomm.np.genevaecommerce.DTO.CollectionAndItemsDTO;

import com.ecomm.np.genevaecommerce.DTO.NewCollectionDTO;
import com.ecomm.np.genevaecommerce.Models.Collection;
import com.ecomm.np.genevaecommerce.Services.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/home")
public class CollectionsController {

    private final HomeService homeService;

    @Autowired
    public CollectionsController(HomeService homeService){
        this.homeService = homeService;
    }

    @GetMapping
    public ResponseEntity<CollectionAndItemsDTO> provideCollection(){
        Collection collection = homeService.saveCollection();
        return ResponseEntity.ok(CollectionAndItemsDTO.buildFromCollection(collection));
    }

}
