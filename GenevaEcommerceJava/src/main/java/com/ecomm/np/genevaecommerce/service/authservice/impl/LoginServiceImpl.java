package com.ecomm.np.genevaecommerce.service.authservice.impl;

import com.ecomm.np.genevaecommerce.model.dto.LoginDTO;
import com.ecomm.np.genevaecommerce.model.dto.LoginResponseDTO;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.Security.CustomUser;
import com.ecomm.np.genevaecommerce.Security.CustomUserService;
import com.ecomm.np.genevaecommerce.Security.JwtUtils;
import com.ecomm.np.genevaecommerce.service.authservice.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    private final CustomUserService customUserService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    public LoginServiceImpl(CustomUserService customUserService,
                            PasswordEncoder passwordEncoder,
                            JwtUtils jwtUtils) {
        this.customUserService = customUserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginResponseDTO login(LoginDTO loginDTO) {
        try {
            CustomUser user = (CustomUser) customUserService.loadUserByUsername(loginDTO.getEmail());
            if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                return new LoginResponseDTO(200, "Logged in successfully", generateJwt(user));
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

    @Override
    public String generateJwt(UserModel user){
        return jwtUtils.generateJwtTokens(CustomUser.build(user));
    }

    private String generateJwt(CustomUser user){
        return jwtUtils.generateJwtTokens(user);
    }
}
