package com.ecomm.np.genevaecommerce.service.authservice;


import com.ecomm.np.genevaecommerce.model.dto.LoginDTO;
import com.ecomm.np.genevaecommerce.model.dto.LoginResponseDTO;
import com.ecomm.np.genevaecommerce.model.dto.PasswordDTO;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;

public interface LoginService {
    LoginResponseDTO login(LoginDTO loginDTO);
    String generateJwt(UserModel user);
}
