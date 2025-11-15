package com.ecomm.np.genevaecommerce.service.checkout;

import com.ecomm.np.genevaecommerce.model.dto.CheckoutIncDTO;
import jakarta.transaction.Transactional;

import java.util.Map;

public interface CheckoutService {
    @Transactional
    Map<String,Object> checkoutOrder(CheckoutIncDTO checkDTO, int userId);
}
