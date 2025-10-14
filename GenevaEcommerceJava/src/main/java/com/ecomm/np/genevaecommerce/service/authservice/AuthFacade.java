package com.ecomm.np.genevaecommerce.service.authservice;

import com.ecomm.np.genevaecommerce.model.dto.*;

/**
 * @author : Asnit Bakhati
 */

public interface AuthFacade {
    LoginResponseDTO login(LoginDTO loginDTO);
    void signUp(SignUpDTO signUpDTO);
    void resendEmail(String email) throws Exception;
    LoginResponseDTO verify(VerificationDTO verificationDTO) throws Exception;
    String changePassword(PasswordDTO passwordDTO, String userEmail);
}
