package com.ecomm.np.genevaecommerce.service.authservice;

import com.ecomm.np.genevaecommerce.model.dto.*;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthFacade {
<<<<<<< HEAD
    LoginResponseDTO login(LoginDTO loginDTO);
    LoginResponseDTO loginAdmin(LoginDTO loginDTO);
=======
>>>>>>> main
    void signUp(SignUpDTO signUpDTO);
    void resendEmail(String email) throws Exception;
    LoginResponseDTO verify(VerificationDTO verificationDTO, HttpServletResponse response) throws Exception;
    String changePassword(PasswordDTO passwordDTO, String userEmail);
}
