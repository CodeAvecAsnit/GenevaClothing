package com.ecomm.np.genevaecommerce.model.events;

import com.ecomm.np.genevaecommerce.model.entity.OrderItemAudit;
import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;

import java.util.List;

public record OrderCreatedEvent(String userEmail, OrderedItems orderedItems, List<OrderItemAudit> items) {
}
