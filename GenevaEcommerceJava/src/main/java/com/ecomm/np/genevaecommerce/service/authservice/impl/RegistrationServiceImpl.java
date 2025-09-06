package com.ecomm.np.genevaecommerce.service.authservice.impl;

import com.ecomm.np.genevaecommerce.model.dto.SignUpDTO;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.model.enumeration.Role;
import com.ecomm.np.genevaecommerce.repository.RoleTableRepository;
import com.ecomm.np.genevaecommerce.service.authservice.RegistrationService;
import com.ecomm.np.genevaecommerce.service.modelservice.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleTableRepository roleTableRepository;
    private final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    public RegistrationServiceImpl(UserService userService,
                                   PasswordEncoder passwordEncoder,
                                   RoleTableRepository roleTableRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleTableRepository = roleTableRepository;
    }

    @Override
    public void validateUserUniqueness(SignUpDTO signUpDTO) {
        UserModel existingUserByEmail = userService.findUserByEmail(signUpDTO.getEmail());
        if (existingUserByEmail != null) {
            throw new UsernameNotFoundException("User already exists with this email");
        }

        UserModel existingUserByName = userService.findUserByName(signUpDTO.getUsername());
        if (existingUserByName != null) {
            throw new UsernameNotFoundException("User already exists with this username");
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
}