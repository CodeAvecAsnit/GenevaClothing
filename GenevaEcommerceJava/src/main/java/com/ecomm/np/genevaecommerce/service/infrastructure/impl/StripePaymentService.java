package com.ecomm.np.genevaecommerce.service.infrastructure.impl;


import com.ecomm.np.genevaecommerce.service.infrastructure.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Asnit Bakhati
 */
@Service
public class StripePaymentService implements PaymentService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${stripe.success-url}")
    private String successUrl;

    @Value("${stripe.cancel-url}")
    private String cancelUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    /**
     * Creates a Stripe Checkout Session and returns the checkout URL
     *
     * @param orderId The order ID
     * @param totalPrice Total price in your currency (will be converted to cents)
     * @param numberOfItems Number of items in the order
     * @param customerEmail Customer's email
     * @param orderAddress Shipping address
     * @return Stripe Checkout URL to redirect to
     * @throws StripeException if Stripe API call fails
     */
    @Override
    public String createCheckoutSession(
            int orderId,
            BigDecimal totalPrice,
            int numberOfItems,
            String customerEmail,
            String orderAddress) throws StripeException {

        long amountInCents = totalPrice.multiply(BigDecimal.valueOf(100)).longValue();

        Map<String, String> metadata = new HashMap<>();
        metadata.put("order_id", String.valueOf(orderId));
        metadata.put("number_of_items", String.valueOf(numberOfItems));
        metadata.put("shipping_address", orderAddress);

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Order #" + orderId)
                                                                .setDescription(numberOfItems + " items")
                                                                .build()
                                                )
                                                .setUnitAmount(amountInCents)
                                                .build()
                                )
                                .setQuantity(1L)
                                .build()
                )
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}&order_id=" + orderId)
                .setCancelUrl(cancelUrl + "?order_id=" + orderId)
                .setCustomerEmail(customerEmail)
                .putAllMetadata(metadata)
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }

    /**
     * Retrieve a checkout session by ID (useful for verifying payment after redirect)
     */
    @Override
    public Session retrieveSession(String sessionId) throws StripeException {
        return Session.retrieve(sessionId);
    }

    /**
     * Verify if payment was successful for a session
     */
    @Override
    public boolean isPaymentSuccessful(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            return "paid".equals(session.getPaymentStatus());
        } catch (StripeException e) {
            return false;
        }
    }
}