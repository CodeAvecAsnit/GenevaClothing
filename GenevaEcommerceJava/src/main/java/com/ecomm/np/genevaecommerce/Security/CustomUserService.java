package com.ecomm.np.genevaecommerce.security;

import com.ecomm.np.genevaecommerce.model.UserModel;
import com.ecomm.np.genevaecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByEmail(username);
        if(user == null)
        {throw new UsernameNotFoundException("Invalid email or password");}
        return CustomUser.build(user);
    }
}
