package com.ecomm.np.genevaecommerce.service.application;

public interface OrderProcessingService {
    boolean setDeliveredAdmin(int orderId);

    boolean setPackedByAdmin(int orderItemCode);

    boolean setAllPackedAdmin(int orderCode);
}
