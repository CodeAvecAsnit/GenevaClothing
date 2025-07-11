package com.ecomm.np.genevaecommerce.Controllers;


import com.ecomm.np.genevaecommerce.DTO.NewCollectionDTO;
import com.ecomm.np.genevaecommerce.Services.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<NewCollectionDTO> provideCollection(){
        return homeService.getALlCollections();
    }

}
