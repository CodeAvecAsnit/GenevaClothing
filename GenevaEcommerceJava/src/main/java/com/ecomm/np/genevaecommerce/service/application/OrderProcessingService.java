package com.ecomm.np.genevaecommerce.service.application;

/**
 * @author : Asnit Bakhati
 */

public interface OrderProcessingService {
    boolean setDeliveredAdmin(int orderId);

    boolean setPackedByAdmin(int orderItemCode);

    boolean setAllPackedAdmin(int orderCode);
}
