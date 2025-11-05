package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.model.dto.*;
import com.ecomm.np.genevaecommerce.security.CustomUser;
import com.ecomm.np.genevaecommerce.service.authservice.AuthFacade;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

/**
 * @author : Asnit Bakhati
 */

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthFacade authFacade;

    @Autowired
    public AuthenticationController(@Qualifier("authFacadeImpl")AuthFacade authFacade) {
        this.authFacade = authFacade;
    }



    @PostMapping("/sign_up/resend")
    public ResponseEntity<BasicDT0> resendCode(@Valid@RequestBody BasicDT0 basicDT0) {
        try {
            authFacade.resendEmail(basicDT0.getMessage());
            return ResponseEntity.ok(new BasicDT0("Verification code has been resent."));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BasicDT0(ex.getMessage()));
        }
    }

    @PostMapping("/sign_up")
    public ResponseEntity<String> SignUp(@RequestBody SignUpDTO signUpDTO) {
        try {
            authFacade.signUp(signUpDTO);
            return ResponseEntity.ok("Verification Code has been sent to : " + signUpDTO.getEmail());
        }catch (RuntimeException ex){
            return ResponseEntity.badRequest().body("Username with this email or username already exists");
        }
    }


    @PostMapping("/sign_up/verify")
    public ResponseEntity<?> verify(@RequestBody VerificationDTO verificationDTO, HttpServletResponse response){
        try{
            LoginResponseDTO verifyAttempt = authFacade.verify(verificationDTO,response);
            if(verifyAttempt.getResponseCode()==200) {
                return ResponseEntity.ok(verifyAttempt);
            }
            else return ResponseEntity.status(403).body(verifyAttempt);
        }
        catch (Exception ex){
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
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Please log in.");
        }
        try {
            String message = authFacade.changePassword(passwordDTO, userDetails.getEmail());
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

}

