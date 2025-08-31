package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.service.AdminStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("api/v1/stats")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminStatisticsController {

    private final AdminStatisticsService adminStatisticsService;

    @Autowired
    public AdminStatisticsController(AdminStatisticsService adminStatisticsService) {
        this.adminStatisticsService = adminStatisticsService;
    }

    @GetMapping("/highest")
    public ResponseEntity<?> highestSelling(){
        return ResponseEntity.ok(adminStatisticsService.findHighestSellingItems());
    }

    @GetMapping("/weekList")
    public ResponseEntity<?> weekListData(){
        return ResponseEntity.ok(adminStatisticsService.findWeekOrders());
    }

    @GetMapping("/data")
    public ResponseEntity<?> allData(){
        return ResponseEntity.ok(adminStatisticsService.findStatsForAdmin());
    }


}
