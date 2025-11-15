package com.ecomm.np.genevaecommerce.service.infrastructure;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import java.math.BigDecimal;

public interface PaymentService {
    String createCheckoutSession(
            int orderId,
            BigDecimal totalPrice,
            int numberOfItems,
            String customerEmail,
            String orderAddress) throws StripeException;

    Session retrieveSession(String sessionId) throws StripeException;

    boolean isPaymentSuccessful(String sessionId);
}
