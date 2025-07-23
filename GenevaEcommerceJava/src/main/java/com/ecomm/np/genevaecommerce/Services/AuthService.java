package com.ecomm.np.genevaecommerce.Services;

import com.ecomm.np.genevaecommerce.DTO.*;
import com.ecomm.np.genevaecommerce.Enumerations.Role;
import com.ecomm.np.genevaecommerce.Models.UserModel;
import com.ecomm.np.genevaecommerce.Repositories.RoleTableRepository;
import com.ecomm.np.genevaecommerce.Repositories.UserRepository;
import com.ecomm.np.genevaecommerce.Security.CustomUser;
import com.ecomm.np.genevaecommerce.Security.CustomUserService;
import com.ecomm.np.genevaecommerce.Security.JwtUtils;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;


@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private Cache<String, SignUpAttempt> codes;

    private Cache<String,SignUpDTO> accounts;

    private final CustomUserService customUserService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final RoleTableRepository roleTableRepository;

    private final JwtUtils jwtUtils;

    private final SecureRandom secureRandom;


    @Autowired
    public AuthService(CustomUserService customUserService, UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService, RoleTableRepository roleTableRepository, JwtUtils jwtUtils, SecureRandom secureRandom) {
        this.customUserService = customUserService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.roleTableRepository = roleTableRepository;
        this.jwtUtils = jwtUtils;
        this.secureRandom = secureRandom;
    }


    @PostConstruct
    public void init(){
        this.codes = Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).maximumSize(1000).build();
        this.accounts = Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).maximumSize(1000).build();
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
    public void signUp(SignUpDTO signUpDTO){
        String mailAddress = signUpDTO.getEmail();
        UserModel ux = userRepository.findByEmail(mailAddress);
        if(ux!=null) return;
        UserModel ui = userRepository.findByUserName(signUpDTO.getUsername());
        if(ui!=null) return ;
        int code = secureRandom.nextInt(100000,1000000);
        mailService.sendVerificationCode(mailAddress,code);
        codes.put(mailAddress,new SignUpAttempt(code));
        accounts.put(mailAddress,signUpDTO);
    }

    public LoginResponseDTO verify(VerificationDTO verificationDTO) throws Exception{
        String email = verificationDTO.getEmail();
        SignUpAttempt attempt = codes.getIfPresent(email);
        if(attempt==null)   throw new Exception("The code has already expired");
        int returnCode = attempt.verificationAttempt(verificationDTO.getCode());
        if(returnCode==-1) throw new Exception("Maximum attempts reached");
        else if (returnCode == 0) {
            return new LoginResponseDTO(403,"The code doesn't match","No jwt");
        }else if(returnCode == 1){
            SignUpDTO userDTO = accounts.getIfPresent(email);
            UserModel user = SignUpDTO.build(userDTO);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoleTable(roleTableRepository.findByRole(Role.USER));
            UserModel savedUser = userRepository.save(user);
            codes.invalidate(email);
            accounts.invalidate(email);
            String jwt = jwtUtils.generateJwtTokens(CustomUser.build(savedUser));
            return new LoginResponseDTO(200,"User has been successfully verified",jwt);
        }else{
            throw new Exception("Unreachable State");
        }
    }
}
