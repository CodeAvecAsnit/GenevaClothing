package com.ecomm.np.genevaecommerce.Controllers;

import com.ecomm.np.genevaecommerce.services.AdminStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("api/v1/stats")
public class AdminStatisticsController {

    private final AdminStatisticsService adminStatisticsService;

    @Autowired
    public AdminStatisticsController(AdminStatisticsService adminStatisticsService) {
        this.adminStatisticsService = adminStatisticsService;
    }

    @GetMapping
    public ResponseEntity<?> getChartOfWeek(){
        return ResponseEntity.ok(adminStatisticsService.getWeekData());
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
