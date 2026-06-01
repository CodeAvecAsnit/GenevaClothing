package com.ecomm.np.genevaecommerce.kakfa;

import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OrderPublisher {
    void publishOrder(String email, OrderedItems orderedItems) throws JsonProcessingException;
}
