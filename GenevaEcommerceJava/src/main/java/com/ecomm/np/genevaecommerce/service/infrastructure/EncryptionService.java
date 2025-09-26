package com.ecomm.np.genevaecommerce.service.infrastructure;

import com.ecomm.np.genevaecommerce.model.dto.OrderData;

public interface EncryptionService {

    String generateOrderData(OrderData orderData) throws Exception;

    OrderData verifyAndDecode(String encryptedJson)throws RuntimeException,Exception;
}
