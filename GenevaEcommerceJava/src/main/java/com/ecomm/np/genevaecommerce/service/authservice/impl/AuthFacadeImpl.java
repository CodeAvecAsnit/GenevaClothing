package com.ecomm.np.genevaecommerce.service.authservice.impl;

import com.ecomm.np.genevaecommerce.model.dto.*;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.service.authservice.AuthFacade;
import com.ecomm.np.genevaecommerce.service.authservice.EmailVerificationService;
import com.ecomm.np.genevaecommerce.service.authservice.LoginService;
import com.ecomm.np.genevaecommerce.service.authservice.RegistrationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AuthFacadeImpl implements AuthFacade {
    private final LoginService loginService;
    private final EmailVerificationService emailVerificationService;
    private final RegistrationService registrationService;

    @Autowired
    public AuthFacadeImpl(LoginServiceImpl loginServiceImpl, EmailVerificationService emailVerificationService, RegistrationServiceImpl registrationServiceImpl) {
        this.loginService = loginServiceImpl;
        this.emailVerificationService = emailVerificationService;
        this.registrationService = registrationServiceImpl;
    }

    @Override
    public LoginResponseDTO login(LoginDTO loginDTO){
        return loginService.login(loginDTO);
    }

    @Async
    @Override
    public void signUp(SignUpDTO signUpDTO){
        registrationService.validateUserUniqueness(signUpDTO);
        emailVerificationService.initiateVerification(signUpDTO);
    }

    @Async
    @Override
    public void resendEmail(String email) throws Exception{
        emailVerificationService.resendVerificationCode(email);
    }

    @Transactional
    @Override
    public LoginResponseDTO verify(VerificationDTO verificationDTO) throws Exception{
        SignUpDTO newUser = emailVerificationService.verifyCode(verificationDTO);
        UserModel user = registrationService.createUser(newUser);
        return new LoginResponseDTO(200,"Sucessfull verified",loginService.generateJwt(user));
    }

    @Transactional
    @Override
    public String changePassword(PasswordDTO passwordDTO, String userEmail){
        return registrationService.registerNewPassword(passwordDTO,userEmail);
    }
}
