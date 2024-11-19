package junkyard.payment.domain.order;


import java.util.List;
import java.util.Optional;

public interface PaymentOrderReader {
    List<PaymentOrder> read(String orderId);
}
