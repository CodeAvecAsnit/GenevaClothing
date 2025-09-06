package com.ecomm.np.genevaecommerce.service.authservice;

import com.ecomm.np.genevaecommerce.model.dto.SignUpDTO;
import com.ecomm.np.genevaecommerce.model.dto.VerificationDTO;

public interface EmailVerificationService {
    SignUpDTO verifyCode(VerificationDTO verificationDTO);
    void initiateVerification(SignUpDTO signUpDTO);
    void resendVerificationCode(String email) throws Exception;
}
