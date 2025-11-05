package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.extra.exception.RateLimitException;
import com.ecomm.np.genevaecommerce.model.dto.LoginDTO;
import com.ecomm.np.genevaecommerce.model.dto.LoginResponseDTO;
import com.ecomm.np.genevaecommerce.service.authservice.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    private final LoginService loginService;
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public LoginController(@Qualifier("loginServiceImpl") LoginService loginService) {
        this.loginService = loginService;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> LoginUser(@RequestBody LoginDTO loginDTO, HttpServletResponse response){
        try{
            LoginResponseDTO loginAttempt = loginService.login(loginDTO,response);
            if(loginAttempt.getResponseCode()==200) {
                return ResponseEntity.ok(loginAttempt);
            } else if (loginAttempt.getResponseCode()==403) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginAttempt);
            }else{
                return ResponseEntity.notFound().build();
            }
        }catch (RateLimitException rEx){
            return ResponseEntity.ok(new LoginResponseDTO(403,rEx.getMessage(),"No Token"));
        }
        catch (Exception ex){
            logger.error(ex.getMessage());
            return ResponseEntity.ok(new LoginResponseDTO(403,"Invalid UserName or Password","No token"));
        }
    }
}
