package com.ecomm.np.genevaecommerce.service.authservice.impl;

import com.ecomm.np.genevaecommerce.model.enumeration.Role;
import com.ecomm.np.genevaecommerce.model.dto.*;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.repository.RoleTableRepository;
import com.ecomm.np.genevaecommerce.security.CustomUser;
import com.ecomm.np.genevaecommerce.security.CustomUserService;
import com.ecomm.np.genevaecommerce.security.JwtUtils;
import com.ecomm.np.genevaecommerce.service.infrastructure.MailService;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.UserServiceImpl;
import com.ecomm.np.genevaecommerce.service.modelservice.UserService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final CustomUserService customUserService;
    private final UserService UserService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final RoleTableRepository roleTableRepository;
    private final JwtUtils jwtUtils;//Generation of JWT token
    private final SecureRandom secureRandom;//Generation of otp code

    private Cache<String, SignUpAttempt> codes;//for rate limiting
    private Cache<String, SignUpDTO> accounts;//for storing accounts


    @Autowired
    public AuthService(CustomUserService customUserService, UserServiceImpl userServiceImpl,
                       PasswordEncoder passwordEncoder,
                       RoleTableRepository roleTableRepository, JwtUtils jwtUtils, SecureRandom secureRandom, MailService mailService) {
        this.customUserService = customUserService;
        this.UserService = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.roleTableRepository = roleTableRepository;
        this.jwtUtils = jwtUtils;
        this.secureRandom = secureRandom;
    }

    @PostConstruct
    public void init(){
        this.codes = Caffeine.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).maximumSize(1000).build();
        this.accounts = Caffeine.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).maximumSize(1000).build();
    }

    private boolean checkPassword(String ps1,String ps2){
        return (passwordEncoder.matches(ps1,ps2));
    }

    public LoginResponseDTO login(LoginDTO loginDTO) {
        try {
            CustomUser user = (CustomUser) customUserService.loadUserByUsername(loginDTO.getEmail());
            if (checkPassword(loginDTO.getPassword(), user.getPassword())) {
                String jwt = jwtUtils.generateJwtTokens(user);
                logger.info("User {} logged in successfully", loginDTO.getEmail());
                return new LoginResponseDTO(200, "Logged in successfully", jwt);
            } else {
                logger.warn("Invalid login attempt for user: {}", loginDTO.getEmail());
                return new LoginResponseDTO(403, "Invalid Username or Password", "");
            }
        } catch (UsernameNotFoundException ex) {
            logger.error("User not found: {}", loginDTO.getEmail(), ex);
            throw new UsernameNotFoundException("Invalid Username or Password");
        } catch (Exception ex) {
            logger.error("Unexpected error during login", ex);
            throw new RuntimeException("An unexpected error occurred", ex);
        }
    }

    @Async
    public void signUp(SignUpDTO signUpDTO) throws UsernameNotFoundException{
        String mailAddress = signUpDTO.getEmail();
        UserModel ux = UserService.findUserByEmail(mailAddress);
        if(ux!=null) throw new UsernameNotFoundException("User already exists with this email");
        UserModel ui = UserService.findUserByName(signUpDTO.getUsername());
        if(ui!=null) throw new UsernameNotFoundException("User already exists with this Username"); ;
        if(codes.getIfPresent(mailAddress)!=null){
            return;
        }
        int code = secureRandom.nextInt(100000,1000000);
        mailService.sendVerificationCode(mailAddress,code);
        codes.put(mailAddress,new SignUpAttempt(code));
        accounts.put(mailAddress,signUpDTO);
    }



    public void resendEmail(String email)throws Exception{
        SignUpAttempt attempt = codes.getIfPresent(email);
        SignUpDTO user =accounts.getIfPresent(email); //resetting for extending its life
        if(attempt==null)
        {
            throw new UsernameNotFoundException("Email may have expired please try signing in again.");
        }
        if(attempt.canSendMail()){
            int code = secureRandom.nextInt(100000,1000000);
            CompletableFuture.runAsync(() -> mailService.sendVerificationCode(email,code));
            attempt.setCode(code);
        }else{
            throw new Exception("Max attempts reached,Sign up after 5 minutes");
        }
    }

    public LoginResponseDTO verify(VerificationDTO verificationDTO) throws Exception{
        String email = verificationDTO.getEmail();
        SignUpAttempt attempt = codes.getIfPresent(email);
        if(attempt==null)   throw new RuntimeException("The code has already expired");
        int returnCode = attempt.verificationAttempt(verificationDTO.getCode());
        if(returnCode==-1) throw new RuntimeException("Maximum attempts reached");
        else if (returnCode == 0) {
            return new LoginResponseDTO(403,"The code doesn't match","No jwt");
        }else if(returnCode == 1){
            SignUpDTO userDTO = accounts.getIfPresent(email);
            UserModel user = SignUpDTO.build(userDTO);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoleTable(roleTableRepository.findByRole(Role.USER));
            UserModel savedUser = UserService.saveUser(user);
            codes.invalidate(email);
            accounts.invalidate(email);
            String jwt = jwtUtils.generateJwtTokens(CustomUser.build(savedUser));
            return new LoginResponseDTO(200,"User has been successfully verified",jwt);
        }else{
            throw new RuntimeException("Unreachable State");
        }
    }

    public String changePassword(PasswordDTO newPass, String email) {
        if (newPass.getNewPassword().equals(newPass.getOldPassword())) {
            throw new BadCredentialsException("New password cannot be the same as the old password.");
        }
        UserModel user = UserService.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found ");
        }
        if (!checkPassword(newPass.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect old password.");
        }
        String encodedPassword = passwordEncoder.encode(newPass.getNewPassword());
        user.setPassword(encodedPassword);
        UserService.saveUser(user);
        logger.info("Password changed successfully for user: {}", email);
        return "Password changed successfully";
    }
}
