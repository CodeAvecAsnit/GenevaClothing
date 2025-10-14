package com.ecomm.np.genevaecommerce.service.checkout;

import com.ecomm.np.genevaecommerce.model.dto.CheckoutIncDTO;
import jakarta.transaction.Transactional;

/**
 * @author : Asnit Bakhati
 */

public interface CheckoutService {
    @Transactional
    boolean checkoutOrder(CheckoutIncDTO checkDTO, int userId);
}
