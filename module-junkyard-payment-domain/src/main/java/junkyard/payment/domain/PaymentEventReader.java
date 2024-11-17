package junkyard.payment.domain;

import java.util.Optional;

public interface PaymentEventReader {
    Optional<PaymentEvent> readByOrderId(String orderId);
}
