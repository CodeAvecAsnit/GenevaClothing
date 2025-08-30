package com.ecomm.np.genevaecommerce;

import com.ecomm.np.genevaecommerce.service.OrderHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDateTime;

@SpringBootTest
@ComponentScan(basePackages = {"com.ecomm.np.genevaecommerce.service"})
public class DateTestForOrderHistory {

    @Autowired
    private OrderHistoryService orderHistoryService;

    private final Logger log = LoggerFactory.getLogger(DateTestForOrderHistory.class);

    @Test
    public void testDateFunction(){
        LocalDateTime localDateTime = LocalDateTime.now();
        String date = orderHistoryService.buildDate(localDateTime);
        System.out.println(date);
    }
}
