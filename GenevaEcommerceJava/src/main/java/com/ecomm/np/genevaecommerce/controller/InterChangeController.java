package com.ecomm.np.genevaecommerce.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/api")
public class InterChangeController {

    @GetMapping("/system")
    @PreAuthorize("hasAuthority('SYSTEM')")
    public String onlySystem(){
        return "anotherSystem";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String onlyAdmin(){
        return "only 4 admin";
    }

    @GetMapping("/hello")
    public String interchange(){
        return "Hello World";
    }
}
