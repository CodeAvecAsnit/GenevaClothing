package com.ecomm.np.genevaecommerce.service.infrastructure.impl;

import com.ecomm.np.genevaecommerce.model.events.OrderCreatedEvent;
import com.ecomm.np.genevaecommerce.service.infrastructure.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author : Asnit Bakhati
 */

@Service
public class CheckoutEventHandler {
    private final MailService mailService;
    private final Logger logger = LoggerFactory.getLogger(CheckoutEventHandler.class);

    public CheckoutEventHandler(@Qualifier("mailServiceImpl") MailService mailService) {
        this.mailService = mailService;
    }

    @EventListener
    @Async
    public void handleOrderCreated(OrderCreatedEvent event) {
        try {
            mailService.sendOrderConfirmationNotice(event.userEmail(),event.orderedItems());
            logger.info("Order confirmation email sent for order: {}", event.orderedItems().getoId());
        } catch (Exception ex) {
            logger.error("Failed to send order confirmation email: {}", ex.getMessage(), ex);
        }
    }
}
