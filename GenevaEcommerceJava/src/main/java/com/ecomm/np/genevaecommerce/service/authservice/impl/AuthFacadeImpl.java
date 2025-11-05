package com.ecomm.np.genevaecommerce.service.authservice.impl;

import com.ecomm.np.genevaecommerce.security.CustomUser;
import com.ecomm.np.genevaecommerce.security.JwtUtils;
import com.ecomm.np.genevaecommerce.model.dto.*;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.service.authservice.AuthFacade;
import com.ecomm.np.genevaecommerce.service.authservice.EmailVerificationService;
import com.ecomm.np.genevaecommerce.service.authservice.RegistrationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AuthFacadeImpl implements AuthFacade {
    private final EmailVerificationService emailVerificationService;
    private final RegistrationService registrationService;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthFacadeImpl(@Qualifier("emailVerificationServiceImpl") EmailVerificationService emailVerificationService,
                          @Qualifier("registrationServiceImpl") RegistrationService registrationService, JwtUtils jwtUtils) {
        this.emailVerificationService = emailVerificationService;
        this.registrationService = registrationService;
        this.jwtUtils = jwtUtils;
    }


    @Async
    @Override
    public void signUp(SignUpDTO signUpDTO){
        if(registrationService.validateUserUniqueness(signUpDTO)) {
            emailVerificationService.initiateVerification(signUpDTO);
        }
    }

    @Async
    @Override
    public void resendEmail(String email) throws Exception{
        emailVerificationService.resendVerificationCode(email);
    }

    @Transactional
    @Override
    public LoginResponseDTO verify(VerificationDTO verificationDTO,HttpServletResponse response) throws Exception{
        SignUpDTO newUser = emailVerificationService.verifyCode(verificationDTO);
        UserModel user = registrationService.createUser(newUser);
        String token = jwtUtils.generateJwtTokens(CustomUser.build(user));
        setJwtCookie(response,token);
        return new LoginResponseDTO(200,"Successfully verified",token);
    }

    @Transactional
    @Override
    public String changePassword(PasswordDTO passwordDTO, String userEmail){
        return registrationService.registerNewPassword(passwordDTO,userEmail);
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
