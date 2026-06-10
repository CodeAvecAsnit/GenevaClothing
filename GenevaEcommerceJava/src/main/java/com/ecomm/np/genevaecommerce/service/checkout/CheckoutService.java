package com.ecomm.np.genevaecommerce.service.checkout;

import com.ecomm.np.genevaecommerce.model.dto.CheckoutIncDTO;
import jakarta.transaction.Transactional;

<<<<<<< HEAD
import java.util.Map;
=======
/**
 * @author : Asnit Bakhati
 */
>>>>>>> main

public interface CheckoutService {
    @Transactional
    Map<String,Object> checkoutOrder(CheckoutIncDTO checkDTO, int userId);
}
