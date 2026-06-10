package com.ecomm.np.genevaecommerce.service.authservice;

import com.ecomm.np.genevaecommerce.model.dto.PasswordDTO;
import com.ecomm.np.genevaecommerce.model.dto.SignUpDTO;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import jakarta.transaction.Transactional;

/**
 * @author : Asnit Bakhati
 */

public interface RegistrationService{
    boolean validateUserUniqueness(SignUpDTO signUpDTO);
    UserModel createUser(SignUpDTO signUpDTO);
    String registerNewPassword(PasswordDTO passwordDTO, String email);
}
