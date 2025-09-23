package com.ecomm.np.genevaecommerce.model.events;

import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;

public record OrderCreatedEvent(String userEmail, OrderedItems orderedItems) {
}
