package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.service.admin.AdminStatisticsService;
import com.ecomm.np.genevaecommerce.service.admin.impl.AdminStatisticsServiceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Asnit Bakhati
 */

@RestController
@RequestMapping("api/v1/stats")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminStatisticsController {

    private final AdminStatisticsService adminStatisticsService;

    @Autowired
    public AdminStatisticsController(AdminStatisticsServiceServiceImpl adminStatisticsService) {
        this.adminStatisticsService = adminStatisticsService;
    }

    @GetMapping("/highest")
    public ResponseEntity<?> highestSelling(){
        return ResponseEntity.ok(adminStatisticsService.findHighestSellingItems());
    }

    @GetMapping("/data")
    public ResponseEntity<?> allData(){
        return ResponseEntity.ok(adminStatisticsService.findStatsForAdmin());
    }

}
