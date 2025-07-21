package com.ecomm.np.genevaecommerce.Controllers;

import com.ecomm.np.genevaecommerce.DTO.LoginDTO;
import com.ecomm.np.genevaecommerce.DTO.LoginResponseDTO;
import com.ecomm.np.genevaecommerce.DTO.SignUpDTO;
import com.ecomm.np.genevaecommerce.DTO.VerificationDTO;
import com.ecomm.np.genevaecommerce.Services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }
    @GetMapping("/wow")
    public String check(){
        return "Success";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO>LoginUser(@RequestBody LoginDTO loginDTO){
        try{
            if(authService.Login(loginDTO)){
                return ResponseEntity.ok(new LoginResponseDTO(200,"Login Successful","Dummy jwt token"));
            }else return ResponseEntity.ok(new LoginResponseDTO(403,"Invalid UserName or Password","No token"));

        }catch (Exception ex){
            logger.error(ex.getMessage());
            return ResponseEntity.ok(new LoginResponseDTO(403,"Invalid UserName or Password","No token"));
        }
    }

    @PostMapping("/sign_up")
    public ResponseEntity<String> SignUp(@RequestBody SignUpDTO signUpDTO) {
        return ResponseEntity.ok(authService.signUp(signUpDTO));
    }


    @PostMapping("/sign_up/verify")
    public ResponseEntity<LoginResponseDTO> verify(@RequestBody VerificationDTO verificationDTO){
        try{
            LoginResponseDTO login = authService.verify(verificationDTO);
            if(login.getResponseCode()==200) return ResponseEntity.ok(login);
            else return ResponseEntity.status(403).body(login);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            LoginResponseDTO dto = new LoginResponseDTO();
            dto.setMessage(ex.getMessage());
            dto.setJwtToken(null);
            dto.setResponseCode(429);
            return ResponseEntity.status(400).body(dto);
        }
    }
   }
