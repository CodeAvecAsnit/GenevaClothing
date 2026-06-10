package com.ecomm.np.genevaecommerce.service.infrastructure;

import com.ecomm.np.genevaecommerce.model.dto.OrderData;

/**
 * @author : Asnit Bakhati
 */

public interface EncryptionService {

    String generateOrderData(OrderData orderData) throws Exception;

    OrderData verifyAndDecode(String encryptedJson)throws RuntimeException,Exception;
}
