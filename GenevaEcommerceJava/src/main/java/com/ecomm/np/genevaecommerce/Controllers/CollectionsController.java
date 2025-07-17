package com.ecomm.np.genevaecommerce.Controllers;


import com.ecomm.np.genevaecommerce.DTO.CollectionAndItemsDTO;


import com.ecomm.np.genevaecommerce.Models.BestCollection;
import com.ecomm.np.genevaecommerce.Models.Collection;
import com.ecomm.np.genevaecommerce.Services.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;


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

    @GetMapping("/best")
    public ResponseEntity<BestCollection> getBest(){
        try{
            return ResponseEntity.ok(homeService.bestCollection());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping
    public ResponseEntity<String> updateBest(@RequestBody BestCollection collection){
        try{
            return ResponseEntity.ok(homeService.updateBestCollection(collection));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Cannot update");
        }
    }
}
