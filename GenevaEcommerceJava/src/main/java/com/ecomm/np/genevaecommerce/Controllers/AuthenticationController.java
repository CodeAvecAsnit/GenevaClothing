package com.ecomm.np.genevaecommerce.Controllers;

import com.ecomm.np.genevaecommerce.DTO.LoginDTO;
import com.ecomm.np.genevaecommerce.DTO.LoginResponseDTO;
import com.ecomm.np.genevaecommerce.DTO.SignUpDTO;
import com.ecomm.np.genevaecommerce.DTO.VerificationDTO;
import com.ecomm.np.genevaecommerce.Services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



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
    public ResponseEntity<LoginResponseDTO>LoginUser(@RequestBody LoginDTO loginDTO,HttpServletResponse response){
        try{
            LoginResponseDTO loginAttempt = authService.login(loginDTO);
            if(loginAttempt.getResponseCode()==200){
                setJwtCookie(response,loginAttempt.getJwtToken());
                return ResponseEntity.ok(loginAttempt);
            }else{
                return ResponseEntity.notFound().build();
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return ResponseEntity.ok(new LoginResponseDTO(403,"Invalid UserName or Password","No token"));
        }
    }

    @PostMapping("/sign_up")
    public ResponseEntity<String> SignUp(@RequestBody SignUpDTO signUpDTO) {
        authService.signUp(signUpDTO);
        return ResponseEntity.ok("Verification Code has been sent to : "+signUpDTO.getEmail());
    }


    @PostMapping("/sign_up/verify")
    public ResponseEntity<LoginResponseDTO> verify(@RequestBody VerificationDTO verificationDTO,HttpServletResponse response){
        try{
            LoginResponseDTO verifyAttempt = authService.verify(verificationDTO);
            if(verifyAttempt.getResponseCode()==200) {
                setJwtCookie(response,verifyAttempt.getJwtToken());
                return ResponseEntity.ok(verifyAttempt);
            }
            else return ResponseEntity.status(403).body(verifyAttempt);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            LoginResponseDTO dto = new LoginResponseDTO();
            dto.setMessage(ex.getMessage());
            dto.setJwtToken(null);
            dto.setResponseCode(429);
            return ResponseEntity.status(400).body(dto);
        }
    }

    private void setJwtCookie(HttpServletResponse response, String jwt) {
        ResponseCookie cookie = ResponseCookie.from("access_token", jwt)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(86400) // 1 day
                .sameSite("Lax")
                .build();
                response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
   }
