package com.ecomm.np.genevaecommerce.service.infrastructure.impl;

import com.ecomm.np.genevaecommerce.service.infrastructure.RateLimiter;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PasswordRateLimiter implements RateLimiter {

    private Cache<String,Integer> limiter;

    public PasswordRateLimiter() {
    }

    @PostConstruct
    public void init() {
        this.limiter = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }

    @Override
    public Optional<Integer> getTries(String email){
        return Optional.ofNullable(limiter.getIfPresent(email));
    }

    @Override
    public void setTries(String email,int tryNumber){
        limiter.put(email,tryNumber);
    }

    @Override
    public void onSuccessRemove(String email,int tryNumber){
        if(tryNumber!=1){
            limiter.invalidate(email);
        }
    }

    @Override
    public void remove(String email) {
        limiter.invalidate(email);
    }
}
