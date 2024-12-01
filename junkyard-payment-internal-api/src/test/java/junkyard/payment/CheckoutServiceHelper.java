package junkyard.payment;

import junkyard.payment.domain.PaymentEvent;
import junkyard.payment.domain.PaymentEventReader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CheckoutServiceHelper {

    @Autowired
    private PaymentEventReader paymentEventReader;

    public PaymentEvent getPaymentEvent(String orderId) {
        return paymentEventReader.readByOrderId(orderId).orElse(null);
    }

}
