package junkyard.payment.domain.order;


import java.util.Optional;

public interface PaymentOrderReader {
    Optional<PaymentOrder> read(String orderId);
}
