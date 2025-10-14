package com.ecomm.np.genevaecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Asnit Bakhati
 */

@RestController
public class InterChangeController {


    @GetMapping("/hello")
    public String interchange(){
        return "Hello World";
    }
}
