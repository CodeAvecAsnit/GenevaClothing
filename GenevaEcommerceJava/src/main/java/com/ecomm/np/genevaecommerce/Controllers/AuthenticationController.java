package com.ecomm.np.genevaecommerce.Controllers;

import com.ecomm.np.genevaecommerce.DTO.*;
import com.ecomm.np.genevaecommerce.Security.CustomUser;
import com.ecomm.np.genevaecommerce.Services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
            if(loginAttempt.getResponseCode()==200) {

                setJwtCookie(response, loginAttempt.getJwtToken()); // asnit
                Cookie cookie = new Cookie("token", "abd");
                cookie.setHttpOnly(true);
                cookie.setSecure(false);
                cookie.setPath("/");
                cookie.setMaxAge(86400);

                response.addCookie(cookie);
                return ResponseEntity.ok(loginAttempt);
            } else if (loginAttempt.getResponseCode()==403) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginAttempt);
        }else{
                return ResponseEntity.notFound().build();
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return ResponseEntity.ok(new LoginResponseDTO(403,"Invalid UserName or Password","No token"));
        }
    }


    @PostMapping("/sign_up/resend")
    public ResponseEntity<BasicDT0> resendCode(@Valid@RequestBody BasicDT0 basicDT0) {
        try {
            authService.resendEmail(basicDT0.getMessage());
            return ResponseEntity.ok(new BasicDT0("Verification code has been resent."));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BasicDT0(ex.getMessage()));
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


    @PutMapping("/update/password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordDTO passwordDTO,
                                            @AuthenticationPrincipal CustomUser userDetails) {
        try {
            String message = authService.changePassword(passwordDTO, userDetails.getEmail());
            return ResponseEntity.ok(new BasicDT0(message));
        } catch (BadCredentialsException ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BasicDT0("Password doesn't match"));
        }
        catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BasicDT0(ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Internal error during password change", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BasicDT0("Something went wrong. Please try again later."));
        }
    }

    private void setJwtCookie(HttpServletResponse response, String jwt) {
        Cookie cookie = new Cookie("access_token", jwt);
                cookie.setHttpOnly(true);
                cookie.setSecure(false);
                cookie.setPath("/");
                cookie.setMaxAge(86400);
                response.addCookie(cookie);
    }
   }
