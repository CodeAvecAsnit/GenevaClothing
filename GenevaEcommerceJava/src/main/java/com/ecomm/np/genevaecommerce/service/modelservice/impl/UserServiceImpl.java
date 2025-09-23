package com.ecomm.np.genevaecommerce.service.modelservice.impl;

import com.ecomm.np.genevaecommerce.extra.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.repository.UserRepository;
import com.ecomm.np.genevaecommerce.service.modelservice.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
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
    @Transactional
    public UserModel saveUser(UserModel user) {
        //can encode password here and remove from others
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean userIsNotRegistered(String email){
        var user = userRepository.findByEmail(email);
        return user.isEmpty();
    }
}
