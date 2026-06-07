package com.ecomm.np.genevaecommerce.kakfa;

import com.ecomm.np.genevaecommerce.model.entity.OrderItemAudit;
import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface OrderPublisher {
    void publishOrder(String email, OrderedItems orderedItems, List<OrderItemAudit> list) throws JsonProcessingException;
}
