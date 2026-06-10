package com.ecomm.np.genevaecommerce.service.infrastructure;

import java.util.Optional;

public interface RateLimiter {

    Optional<Integer> getTries(String email);

    void setTries(String email, int tryNumber);

    void onSuccessRemove(String email, int tryNumber);

    void remove(String email);
}
