package com.ecomm.np.genevaecommerce.security;

import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.service.modelservice.UserService;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public CustomUserService(UserServiceImpl userServiceImpl){
        this.userService = userServiceImpl;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userService.findUserByEmail(username);
        return CustomUser.build(userModel);
    }
}
