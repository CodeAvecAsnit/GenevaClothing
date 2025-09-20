package com.ecomm.np.genevaecommerce.service;

import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import com.ecomm.np.genevaecommerce.repository.UserRepository;
import com.ecomm.np.genevaecommerce.service.modelservice.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    public UserServiceImplTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindUserByEmail() {
        UserModel mockUser = new UserModel();
        mockUser.setEmail("asnitbakhati@gmail.com");
        when(userRepository.findByEmail(mockUser.getEmail()))
                .thenReturn(Optional.of(mockUser));
        UserModel foundUser = userService.findUserByEmail("asnitbakhati@gmail.com");
        assertNotNull(foundUser);
    }

    @Test
    public void testFindUserById(){
        UserModel mockUser = new UserModel();
        mockUser.setUserId(1);
        when(userRepository.findById(mockUser.getUserId())).thenReturn(Optional.of(mockUser));
        UserModel foundUser = userService.findUserById(1);
        assertNotNull(foundUser);
    }
}

