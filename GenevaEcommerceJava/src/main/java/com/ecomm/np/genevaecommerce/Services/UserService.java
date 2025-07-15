package com.ecomm.np.genevaecommerce.Services;

import com.ecomm.np.genevaecommerce.DTO.UserDTO;
import com.ecomm.np.genevaecommerce.Enumerations.Role;
import com.ecomm.np.genevaecommerce.Models.RoleTable;
import com.ecomm.np.genevaecommerce.Models.UserModel;
import com.ecomm.np.genevaecommerce.Repositories.RoleTableRepository;
import com.ecomm.np.genevaecommerce.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleTableRepository roleTableRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleTableRepository roleTableRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleTableRepository = roleTableRepository;
    }

    public UserModel saveUser(UserDTO userDTO) {
        UserModel appUser = UserDTO.userModelBuild(userDTO);
        try {
            Role role = Role.valueOf("USER");
            roleTableRepository.findByRole(role).ifPresent(appUser::setRoleTable);
        } catch (IllegalArgumentException ex) {
            logger.error("Cannot Parse the String. ");
        }
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return userRepository.save(appUser);
    }

}

