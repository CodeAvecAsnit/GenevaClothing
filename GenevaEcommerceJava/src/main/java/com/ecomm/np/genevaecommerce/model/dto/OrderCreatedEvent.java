package com.ecomm.np.genevaecommerce.model.dto;

import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;

public record OrderCreatedEvent(String userEmail, OrderedItems orderedItems) {
}
