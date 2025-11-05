package com.ecomm.np.genevaecommerce.service.authservice.impl;

import com.ecomm.np.genevaecommerce.extra.exception.RateLimitException;
import com.ecomm.np.genevaecommerce.model.dto.LoginDTO;
import com.ecomm.np.genevaecommerce.model.dto.LoginResponseDTO;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.security.CustomUser;
import com.ecomm.np.genevaecommerce.security.CustomUserService;
import com.ecomm.np.genevaecommerce.security.JwtUtils;
import com.ecomm.np.genevaecommerce.service.authservice.LoginService;
import com.ecomm.np.genevaecommerce.service.infrastructure.RateLimiter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    private final RateLimiter rateLimiter;
    private final CustomUserService customUserService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    public LoginServiceImpl(@Qualifier("passwordRateLimiter") RateLimiter rateLimiter,
                            CustomUserService customUserService,
                            PasswordEncoder passwordEncoder,
                            JwtUtils jwtUtils) {
        this.rateLimiter = rateLimiter;
        this.customUserService = customUserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    private boolean checkPassword(String enteredPass,String dbPass){
        return passwordEncoder.matches(enteredPass,dbPass);
    }

    @Override
    public LoginResponseDTO login(LoginDTO loginDTO, HttpServletResponse response)throws RateLimitException {
        Integer attemptNo = checkLimit(loginDTO.getEmail());

        try {
            CustomUser user = (CustomUser) customUserService.loadUserByUsername(loginDTO.getEmail());
            if (checkPassword(loginDTO.getPassword(), user.getPassword())) {
                String token = generateJwt(user);
                setJwtCookie(response,token);
                rateLimiter.onSuccessRemove(loginDTO.getEmail(),attemptNo);
                return new LoginResponseDTO(200, "Logged in successfully", token);
            } else {
                logger.warn("Invalid login attempt for user: {}", loginDTO.getEmail());
                rateLimiter.setTries(loginDTO.getEmail(), ++attemptNo);
                return new LoginResponseDTO(403, "Invalid Username or Password", "");
            }
        } catch (UsernameNotFoundException ex) {
            rateLimiter.setTries(loginDTO.getEmail(), ++attemptNo);
            logger.error("User not found: {}", loginDTO.getEmail(), ex);
            throw new UsernameNotFoundException("Invalid Username or Password");
        } catch (Exception ex) {
            logger.error("Unexpected error during login", ex);
            throw new RuntimeException("An unexpected error occurred", ex);
        }
    }


    private String generateJwt(CustomUser user){
        return jwtUtils.generateJwtTokens(user);
    }

    private void setJwtCookie(HttpServletResponse response, String jwt) {
        Cookie cookie = new Cookie("access_token", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);
    }

    private Integer checkLimit(String email){
        int x = 1;
        Optional<Integer> num = rateLimiter.getTries(email);
        if(num.isPresent()){
            x = num.get();
            if(x>5){
                throw new RateLimitException("Out of tries");
            }
        }
        return x;
    }

}
