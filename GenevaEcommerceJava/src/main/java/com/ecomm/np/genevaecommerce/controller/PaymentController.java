package com.ecomm.np.genevaecommerce.controller;

import com.ecomm.np.genevaecommerce.service.infrastructure.PaymentService;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {


    private final PaymentService paymentService;

    @Autowired
    public PaymentController(@Qualifier("stripePaymentService") PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/success")
    public String paymentSuccess(
            @RequestParam("session_id") String sessionId,
            @RequestParam("order_id") int orderId,
            Model model) {

        try {
            Session session = paymentService.retrieveSession(sessionId);
            boolean isPaid = "paid".equals(session.getPaymentStatus());

            if (isPaid) {
                // TODO: Update order status in database to "PAID"

                model.addAttribute("success", true);
                model.addAttribute("orderId", orderId);
                model.addAttribute("message", "Payment successful!");
                return "payment-success";
            } else {
                model.addAttribute("success", false);
                model.addAttribute("message", "Payment not completed");
                return "payment-failed";
            }
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("error", e.getMessage());
            return "payment-failed";
        }


    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
        String eventType = (String) payload.get("type");

        System.out.println("Received webhook event: " + eventType);

        switch (eventType) {
            case "payment_intent.succeeded":
                System.out.println("Payment succeeded!");
                break;
            case "payment_intent.payment_failed":
                System.out.println("Payment failed!");
                break;
            case "charge.refunded":
                System.out.println("Refund processed!");
                break;
            default:
                System.out.println("Unhandled event type: " + eventType);
        }
        return ResponseEntity.ok("Webhook received");
    }

    @GetMapping("/cancel")
    public String paymentCancel(@RequestParam("order_id") int orderId, Model model) {
        // TODO: Update order status to "CANCELLED" or "PENDING"
        model.addAttribute("orderId", orderId);
        model.addAttribute("message", "Payment was cancelled");
        return "redirect:http://127.0.0.1:5500/orderview.html?id=" + orderId;
    }
}