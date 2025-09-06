package com.ecomm.np.genevaecommerce.service.authservice;

import com.ecomm.np.genevaecommerce.model.dto.SignUpDTO;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;

public interface RegistrationService{
    void validateUserUniqueness(SignUpDTO signUpDTO);
    UserModel createUser(SignUpDTO signUpDTO);
}
