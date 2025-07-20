package com.ecomm.np.genevaecommerce.Controllers;

import com.ecomm.np.genevaecommerce.DTO.CollectionAndItemsDTO;
import com.ecomm.np.genevaecommerce.Mail.MailService;
import com.ecomm.np.genevaecommerce.Models.BestCollection;
import com.ecomm.np.genevaecommerce.Models.Collection;
import com.ecomm.np.genevaecommerce.Services.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/home")
public class CollectionsController {

    private final HomeService homeService;
    private final MailService mailService;

    @Autowired
    public CollectionsController(HomeService homeService, MailService mailService) {
        this.homeService = homeService;
        this.mailService = mailService;
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

    @GetMapping("/code")
    public ResponseEntity<Integer> sendCode(@RequestParam String email) {
        return ResponseEntity.ok(mailService.sendVerificationCode(email));
    }

    @GetMapping("/batch")
    public int sendMails() {
        List<String> mailList = new ArrayList<>();
        mailList.add("asnitbakhati@gmail.com");
        mailList.add("aranzabakhati@gmail.com");
        mailList.add("csit23081031_asnit@achsnepal.edu.np");

        String title = "Ils sont un test";
        String body = "Bonjour. Ceci est un test pour le service de mail.";
        mailService.sendPromotionalEmail(mailList, title, body);
        return 1;
    }
}
