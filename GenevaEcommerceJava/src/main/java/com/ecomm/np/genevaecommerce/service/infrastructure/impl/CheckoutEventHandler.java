package com.ecomm.np.genevaecommerce.service.infrastructure.impl;

import com.ecomm.np.genevaecommerce.kakfa.OrderPublisher;
import com.ecomm.np.genevaecommerce.model.events.OrderCreatedEvent;
import com.ecomm.np.genevaecommerce.service.infrastructure.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CheckoutEventHandler {
    private final MailService mailService;
    private final OrderPublisher orderPublisher;
    private final Logger logger = LoggerFactory.getLogger(CheckoutEventHandler.class);

    public CheckoutEventHandler(@Qualifier("mailServiceImpl") MailService mailService,
                                @Qualifier("orderPublisherImpl") OrderPublisher orderPublisher) {
        this.mailService = mailService;
        this.orderPublisher = orderPublisher;
    }

    @EventListener
    @Async
    public void handleOrderCreated(OrderCreatedEvent event) {

        try {
            orderPublisher.publishOrder(event.userEmail(),event.orderedItems());
        } catch (Exception ex) {
                logger.info("Cannot publish the event");
        } finally {
            try {
                mailService.sendOrderConfirmationNotice(event.userEmail(), event.orderedItems());
                logger.info("Order confirmation email sent for order: {}", event.orderedItems().getoId());
            } catch (Exception ex) {
                logger.error("Failed to send order confirmation email: {}", ex.getMessage(), ex);
            }
        }
    }
}
