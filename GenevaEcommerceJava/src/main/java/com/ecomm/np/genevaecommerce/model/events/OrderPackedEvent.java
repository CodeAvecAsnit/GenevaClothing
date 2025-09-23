package com.ecomm.np.genevaecommerce.model.events;

import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;

public record OrderPackedEvent(String orderEmail, OrderedItems items) {
}
