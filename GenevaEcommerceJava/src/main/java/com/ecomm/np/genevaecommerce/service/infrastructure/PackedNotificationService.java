package com.ecomm.np.genevaecommerce.service.infrastructure;

import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;
import org.springframework.scheduling.annotation.Async;

/**
 * @author : Asnit Bakhati
 */

public interface PackedNotificationService {
    @Async
    void sendPackedNotice(String email, OrderedItems orderedItems);
}
