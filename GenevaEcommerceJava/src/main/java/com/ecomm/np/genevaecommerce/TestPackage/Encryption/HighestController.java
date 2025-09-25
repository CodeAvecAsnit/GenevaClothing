package com.ecomm.np.genevaecommerce.TestPackage.Encryption;

import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.dto.ItemDisplayDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/top-selling")
public class HighestController {

    private final HighestService highestService;

    @Autowired
    public HighestController(HighestService highestService) {
        this.highestService = highestService;
    }

    @GetMapping("/high")
    public ResponseEntity<List<ItemDisplayDTO>> findTopSellers(){
        try{
            return ResponseEntity.ok(highestService.findHighestSellingItems());
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.badRequest().build();
         }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/random")
    public ResponseEntity<List<ItemDisplayDTO>> findRandomItems(){
        try{
            return ResponseEntity.ok(highestService.findRandomItems());
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.badRequest().build();
        }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }
}
