package com.ecomm.np.genevaecommerce.service.authservice.impl;

import com.ecomm.np.genevaecommerce.extra.exception.RateLimitException;
import com.ecomm.np.genevaecommerce.model.dto.SignUpDTO;
import com.ecomm.np.genevaecommerce.model.dto.VerificationDTO;
import com.ecomm.np.genevaecommerce.service.authservice.EmailVerificationService;
import com.ecomm.np.genevaecommerce.service.infrastructure.MailService;
import com.ecomm.np.genevaecommerce.service.infrastructure.RateLimiter;
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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private final MailService mailService;
    private final SecureRandom secureRandom;

    private final RateLimiter rateLimiter;
    private Cache<String, SignUpDTO> pendingAccounts;
    private final Logger logger = LoggerFactory.getLogger(EmailVerificationServiceImpl.class);

    @Autowired
    public EmailVerificationServiceImpl(@Qualifier("mailServiceImpl") MailService mailService, SecureRandom secureRandom, RateLimiter rateLimiter) {
        this.mailService = mailService;
        this.secureRandom = secureRandom;
        this.rateLimiter = rateLimiter;
    }

    @PostConstruct
    public void initializeCaches() {
        this.pendingAccounts = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }

    @Async
    public void initiateVerification(SignUpDTO signUpDTO) {
        String email = signUpDTO.getEmail();
        if (pendingAccounts.getIfPresent(email) != null) {
            logger.warn("Verification already in progress for email: {}", email);
            return;
        }
        int code = generateVerificationCode();
        signUpDTO.setCode(code);
        mailService.sendVerificationCode(email, code);
        pendingAccounts.put(email, signUpDTO);
        logger.info("Verification code sent to: {}", email);
    }

    @Async
    public void resendVerificationCode(String email) throws Exception {
        SignUpDTO attempt = pendingAccounts.getIfPresent(email);
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
        rateLimiter.remove(email);
        pendingAccounts.invalidate(email);
    }

    private int generateVerificationCode() {
        return secureRandom.nextInt(100000, 1000000);
    }

    @Override
    public SignUpDTO verifyCode(VerificationDTO verificationDTO) {
        String email = verificationDTO.getEmail();
        SignUpDTO attempt = pendingAccounts.getIfPresent(email);
        if (attempt == null) {
            throw new RuntimeException("Verification code has expired or does not exist");
        }
        int tryNumber = checkLimitation(email);
        if(attempt.getCode()==verificationDTO.getCode()){
            clearVerificationData(email);
            rateLimiter.onSuccessRemove(email,tryNumber);
            return attempt;
        }else{
            rateLimiter.setTries(email,++tryNumber);
            throw new BadCredentialsException("Verification code does not match");
        }
    }

    private Integer checkLimitation(String email){
        int num = 1;
        Optional<Integer> opt = rateLimiter.getTries(email);
        if(opt.isPresent()){
            num = opt.get();
            if(num>5){
                throw new RateLimitException("Max Limit reached.Try again later");
            }
        }
        return num;
    }
}
