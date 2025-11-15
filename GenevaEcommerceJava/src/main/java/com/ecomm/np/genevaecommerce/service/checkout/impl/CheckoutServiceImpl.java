package com.ecomm.np.genevaecommerce.service.checkout.impl;

import com.ecomm.np.genevaecommerce.model.dto.CheckoutIncDTO;
import com.ecomm.np.genevaecommerce.model.events.OrderCreatedEvent;
import com.ecomm.np.genevaecommerce.model.entity.*;
import com.ecomm.np.genevaecommerce.service.checkout.CheckoutService;
import com.ecomm.np.genevaecommerce.service.checkout.OrderCreationService;
import com.ecomm.np.genevaecommerce.service.infrastructure.PaymentService;
import com.ecomm.np.genevaecommerce.service.modelservice.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final Logger logger = LoggerFactory.getLogger(CheckoutServiceImpl.class);
    private final UserService userService;
    private final OrderCreationService orderCreationService;
    private final ApplicationEventPublisher eventPublisher;
    private final PaymentService paymentService;

    @Autowired
    public CheckoutServiceImpl(@Qualifier("userServiceImpl") UserService userService,
                               @Qualifier("orderCreationServiceImpl") OrderCreationServiceImpl orderCreationServiceImpl,
                               ApplicationEventPublisher eventPublisher,
                               @Qualifier("stripePaymentService") PaymentService paymentService) {
        this.userService = userService;
        this.orderCreationService = orderCreationServiceImpl;
        this.eventPublisher = eventPublisher;
        this.paymentService = paymentService;
    }

    @Override
    public Map<String, Object> checkoutOrder(CheckoutIncDTO checkDTO, int userId) {
        logger.info("Starting checkout process: {}", checkDTO.toString());
        Map<String, Object> response = new HashMap<>();

        try {
            UserModel userModel = userService.findUserById(userId);
            OrderedItems savedOrder = orderCreationService.createOrder(checkDTO, userModel);

            if(checkDTO.getPaymentMethod().equals("OPENPAY")){
                String checkoutUrl = redirectToStripe(savedOrder);
                response.put("success", true);
                response.put("redirectUrl", checkoutUrl);
                response.put("orderId", savedOrder.getoId());
                logger.info("Stripe checkout URL created for order: {}", savedOrder.getoId());
            } else {
                // COD payment
                eventPublisher.publishEvent(new OrderCreatedEvent(userModel.getEmail(), savedOrder));
                response.put("success", true);
                response.put("message", "Order placed successfully with COD");
                response.put("orderId", savedOrder.getoId());
                logger.info("COD order completed successfully for user: {}", userId);
            }

            return response;
        } catch (Exception ex) {
            logger.error("Checkout failed for user {}: {}", userId, ex.getMessage(), ex);
            response.put("success", false);
            response.put("error", ex.getMessage());
            return response;
        }
    }

    private String redirectToStripe(OrderedItems orderedItems) {
        try {
            int orderId = orderedItems.getoId();
            BigDecimal price = orderedItems.getTotalPrice();
            int noOfItems = orderedItems.getTotalItems();
            String orderAddress = orderedItems.getOrderDetails().getFinalLocation();
            String email = orderedItems.getOrderDetails().getUser().getEmail();

            return paymentService.createCheckoutSession(
                    orderId,
                    price,
                    noOfItems,
                    email,
                    orderAddress
            );
        } catch (Exception e) {
            logger.error("Failed to create Stripe checkout session: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initiate Stripe payment", e);
        }
    }
}
