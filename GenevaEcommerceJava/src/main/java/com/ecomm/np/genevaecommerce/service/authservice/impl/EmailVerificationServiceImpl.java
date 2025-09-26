package com.ecomm.np.genevaecommerce.service.authservice.impl;

import com.ecomm.np.genevaecommerce.extra.exception.ExpiryError;
import com.ecomm.np.genevaecommerce.extra.exception.ResourceNotFoundException;
import com.ecomm.np.genevaecommerce.model.dto.SignUpAttempt;
import com.ecomm.np.genevaecommerce.model.dto.SignUpDTO;
import com.ecomm.np.genevaecommerce.model.dto.VerificationDTO;
import com.ecomm.np.genevaecommerce.service.authservice.EmailVerificationService;
import com.ecomm.np.genevaecommerce.service.infrastructure.MailService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private final MailService mailService;
    private final SecureRandom secureRandom;

    private Cache<String, SignUpAttempt> verificationCodes;
    private Cache<String, SignUpDTO> pendingAccounts;
    private final Logger logger = LoggerFactory.getLogger(EmailVerificationServiceImpl.class);

    @Autowired
    public EmailVerificationServiceImpl(@Qualifier("mailServiceImpl") MailService mailService, SecureRandom secureRandom) {
        this.mailService = mailService;
        this.secureRandom = secureRandom;
    }

    @PostConstruct
    public void initializeCaches() {
        this.verificationCodes = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
        this.pendingAccounts = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }

    @Async
    public void initiateVerification(SignUpDTO signUpDTO) {
        String email = signUpDTO.getEmail();
        if (verificationCodes.getIfPresent(email) != null) {
            logger.warn("Verification already in progress for email: {}", email);
            return;
        }
        int code = generateVerificationCode();
        mailService.sendVerificationCode(email, code);
        verificationCodes.put(email, new SignUpAttempt(code));
        pendingAccounts.put(email, signUpDTO);
        logger.info("Verification code sent to: {}", email);
    }

    @Async
    public void resendVerificationCode(String email) throws Exception {
        SignUpAttempt attempt = verificationCodes.getIfPresent(email);
        if (attempt == null) {
            throw new UsernameNotFoundException("Email verification session expired. Please sign up again.");
        }
        if (attempt.canSendMail()) {
            int newCode = generateVerificationCode();
            CompletableFuture.runAsync(() -> mailService.sendVerificationCode(email, newCode));
            attempt.setCode(newCode);
            logger.info("Verification code resent to: {}", email);
        } else {
            throw new RuntimeException("Maximum attempts reached. Please try again after 5 minutes.");
        }
    }

    private void clearVerificationData(String email) {
        verificationCodes.invalidate(email);
        pendingAccounts.invalidate(email);
    }

    private int generateVerificationCode() {
        return secureRandom.nextInt(100000, 1000000);
    }

    @Override
    public SignUpDTO verifyCode(VerificationDTO verificationDTO) {
        String email = verificationDTO.getEmail();
        SignUpAttempt attempt = verificationCodes.getIfPresent(email);
        if (attempt == null) {
            throw new RuntimeException("Verification code has expired or does not exist");
        }
        int responseCode = attempt.verificationAttempt(verificationDTO.getCode());
        switch (responseCode) {
            case -1:
                clearVerificationData(email);
                throw new ExpiryError("Verification code has already expired");
            case 0:
                throw new BadCredentialsException("Verification code does not match");
            case 1:
                SignUpDTO signUpDTO = pendingAccounts.getIfPresent(email);
                if (signUpDTO == null) {
                    throw new ResourceNotFoundException("User not found for provided email");
                }
                clearVerificationData(email);
                return signUpDTO;
            default:
                throw new IllegalStateException("Unexpected verification response code: " + responseCode);
        }
    }
}
