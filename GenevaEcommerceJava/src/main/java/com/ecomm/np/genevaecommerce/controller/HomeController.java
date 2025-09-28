package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.extra.exception.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.dto.CollectionAndItemsDTO;
import com.ecomm.np.genevaecommerce.model.dto.ItemDisplayDTO;
import com.ecomm.np.genevaecommerce.model.entity.BestCollection;
import com.ecomm.np.genevaecommerce.model.entity.Collection;
import com.ecomm.np.genevaecommerce.Security.CustomUser;
import com.ecomm.np.genevaecommerce.service.application.BasicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/home")
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private final BasicService basicService;

    @Autowired
    public HomeController(@Qualifier("basicServiceImpl") BasicService basicService) {
        this.basicService = basicService;
    }

    @GetMapping
    public ResponseEntity<CollectionAndItemsDTO> provideCollection() {
        Collection collection = basicService.saveCollection();
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

    @GetMapping("/check")
    public ResponseEntity<Boolean> responseEntity(@AuthenticationPrincipal CustomUser customUser){
        Boolean isAdmin = customUser.getAuthorities().stream().anyMatch(auth->auth.getAuthority().equals("ADMIN"));
        return ResponseEntity.ok(isAdmin);
    }

    @GetMapping("/high")
    public ResponseEntity<List<ItemDisplayDTO>> findTopSellers(){
        try{
            return ResponseEntity.ok(basicService.findHighestSellingItems());
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.badRequest().build();
        }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/random")
    public ResponseEntity<List<ItemDisplayDTO>> findRandomItems(){
        try{
            return ResponseEntity.ok(basicService.findRandomItems());
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.badRequest().build();
        }catch (Exception ex){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/latest")
    public ResponseEntity<List<ItemDisplayDTO>> latestItems() {
        return ResponseEntity.ok(basicService.displayNewArrivals());
    }

    @GetMapping("/health")
    public String health() {
        return "App is running!";
    }


    @GetMapping("/test-db")
    public Map
            <String, Object> testDatabase() {
        Map<String, Object> result = new HashMap<>();

        String url = "jdbc:postgresql://db.yzhmojktlemvrmbfrmta.supabase.co:5432/postgres?sslmode=require";
        String username = "postgres";
        String password = "ZS8fFnVHMVp1Duge";

        try {
            // Test connection
            Connection connection = DriverManager.getConnection(url, username, password);
            result.put("connection", "SUCCESS");

            // Test simple query
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT version()");
            if (rs.next()) {
                result.put("database_version", rs.getString(1));
            }

            // Test table access
            try {
                ResultSet tables = stmt.executeQuery("SELECT count(*) FROM information_schema.tables WHERE table_schema = 'public'");
                if (tables.next()) {
                    result.put("public_tables_count", tables.getInt(1));
                }
            } catch (Exception e) {
                result.put("tables_error", e.getMessage());
            }

            connection.close();
            result.put("status", "Database connection successful!");

        } catch (Exception e) {
            result.put("connection", "FAILED");
            result.put("error", e.getMessage());
            result.put("error_class", e.getClass().getSimpleName());
        }

        return result;
    }

}
