package com.ecomm.np.genevaecommerce.service.authservice.impl;

import com.ecomm.np.genevaecommerce.model.dto.PasswordDTO;
import com.ecomm.np.genevaecommerce.model.dto.SignUpDTO;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.model.enumeration.Role;
import com.ecomm.np.genevaecommerce.repository.RoleTableRepository;
import com.ecomm.np.genevaecommerce.service.authservice.RegistrationService;
import com.ecomm.np.genevaecommerce.service.modelservice.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author : Asnit Bakhati
 */

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleTableRepository roleTableRepository;
    private final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    public RegistrationServiceImpl(@Qualifier("userServiceImpl") UserService userService,
                                   PasswordEncoder passwordEncoder,
                                   RoleTableRepository roleTableRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleTableRepository = roleTableRepository;
    }

    @Override
    public void validateUserUniqueness(SignUpDTO signUpDTO) {
        if(!userService.userIsNotRegistered(signUpDTO.getEmail())){
            throw new RuntimeException("User is already registered");
        }
    }

    @Override
    @Transactional
    public UserModel createUser(SignUpDTO signUpDTO) {
        UserModel user = SignUpDTO.build(signUpDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleTable(roleTableRepository.findByRole(Role.USER));
        UserModel savedUser = userService.saveUser(user);
        logger.info("New user created successfully: {}", signUpDTO.getEmail());
        return savedUser;
    }

    @Override
    @Transactional
    public String registerNewPassword(PasswordDTO passwordDTO, String email) {
        if (passwordDTO.getNewPassword().equals(passwordDTO.getOldPassword())) {
            throw new BadCredentialsException("New password cannot be the same as the old password.");
        }
        UserModel user = userService.findUserByEmail(email);
        if (!passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect old password.");
        }
        String encodedPassword = passwordEncoder.encode(passwordDTO.getNewPassword());
        user.setPassword(encodedPassword);
        userService.saveUser(user);
        logger.info("Password changed successfully for user: {}", email);
        return "Password changed successfully";
    }
}