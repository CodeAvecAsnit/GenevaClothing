package com.ecomm.np.genevaecommerce.service.modelservice;

import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserModel findUserById(int userid){
        return userRepository.findById(userid).
                orElseThrow(()-> new UsernameNotFoundException("the user want not found"));
    }

    @Override
    @Transactional
    public UserModel findUserByEmail(String email) {
        return userRepository.findByEmail(email).
                orElseThrow(()-> new ResourceNotFoundException("the user want not found"));
    }

    @Override
    @Transactional
    public UserModel findUserByName(String userName) {
        return  userRepository.findByUserName(userName);
    }

    @Override
    public UserModel saveUser(UserModel user) {
        return userRepository.save(user);
    }

}
