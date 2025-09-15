package com.ecomm.np.genevaecommerce.service.application;

import com.ecomm.np.genevaecommerce.model.dto.CheckoutIncDTO;
import jakarta.transaction.Transactional;

public interface CheckoutService {
    @Transactional
    boolean checkoutOrder(CheckoutIncDTO checkDTO, int userId);
}
