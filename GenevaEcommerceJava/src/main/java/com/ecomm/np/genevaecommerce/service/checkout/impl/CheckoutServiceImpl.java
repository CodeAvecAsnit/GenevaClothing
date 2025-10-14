package com.ecomm.np.genevaecommerce.service.checkout.impl;

import com.ecomm.np.genevaecommerce.model.dto.CheckoutIncDTO;
import com.ecomm.np.genevaecommerce.model.events.OrderCreatedEvent;
import com.ecomm.np.genevaecommerce.model.entity.*;
import com.ecomm.np.genevaecommerce.service.checkout.CheckoutService;
import com.ecomm.np.genevaecommerce.service.checkout.OrderCreationService;
import com.ecomm.np.genevaecommerce.service.modelservice.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;


/**
 * @author : Asnit Bakhati
 */

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final Logger logger = LoggerFactory.getLogger(CheckoutServiceImpl.class);
    private final UserService userService;
    private final OrderCreationService orderCreationService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public CheckoutServiceImpl(@Qualifier("userServiceImpl") UserService userService,
                               @Qualifier("orderCreationServiceImpl") OrderCreationServiceImpl orderCreationServiceImpl,
                               ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.orderCreationService = orderCreationServiceImpl;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public boolean checkoutOrder(CheckoutIncDTO checkDTO, int userId) {
        logger.info("Starting checkout process: {}", checkDTO.toString());
        try {
            UserModel userModel = userService.findUserById(userId);
            OrderedItems savedOrder = orderCreationService.createOrder(checkDTO, userModel);
            eventPublisher.publishEvent(new OrderCreatedEvent(userModel.getEmail(), savedOrder));
            logger.info("Checkout completed successfully for user: {}", userId);
            return true;
        } catch (Exception ex) {
            logger.error("Checkout failed for user {}: {}", userId, ex.getMessage(), ex);
            return false;
        }
    }
}
