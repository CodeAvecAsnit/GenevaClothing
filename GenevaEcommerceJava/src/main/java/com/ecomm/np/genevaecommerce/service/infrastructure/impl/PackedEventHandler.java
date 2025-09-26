package com.ecomm.np.genevaecommerce.service.infrastructure.impl;

import com.ecomm.np.genevaecommerce.model.events.OrderPackedEvent;
import com.ecomm.np.genevaecommerce.service.infrastructure.PackedNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PackedEventHandler {
    private final Logger logger = LoggerFactory.getLogger(PackedEventHandler.class);
    private final PackedNotificationService packedNotificationService;

    @Autowired
    public PackedEventHandler(@Qualifier("mailNotificationServiceImpl") PackedNotificationService packedNotificationService) {
        this.packedNotificationService = packedNotificationService;
    }

    @EventListener
    @Async
    public void sendPackedMail(OrderPackedEvent event){
        try {
            packedNotificationService.sendPackedNotice(event.orderEmail(),event.items());
        }catch (RuntimeException ex){
            logger.error(ex.getMessage());
        }
    }
}
