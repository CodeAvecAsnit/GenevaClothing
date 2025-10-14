package com.ecomm.np.genevaecommerce.service.checkout;

import com.ecomm.np.genevaecommerce.model.dto.CheckoutIncDTO;
import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;
import com.ecomm.np.genevaecommerce.model.entity.UserModel;
import jakarta.transaction.Transactional;

/**
 * @author : Asnit Bakhati
 */

public interface OrderCreationService {

    @Transactional
    OrderedItems createOrder(CheckoutIncDTO checkDTO, UserModel userModel);
}
