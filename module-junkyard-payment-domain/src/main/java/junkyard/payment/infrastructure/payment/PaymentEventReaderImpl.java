package junkyard.payment.infrastructure.payment;

import junkyard.payment.domain.PaymentEvent;
import junkyard.payment.domain.PaymentEventReader;
import junkyard.payment.domain.order.PaymentOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PaymentEventReaderImpl implements PaymentEventReader {
    private final PaymentEventRepository paymentEventRepository;
    @Override
    public Optional<PaymentEvent> readByOrderId(String orderId) {
        return paymentEventRepository.readByOrderId(orderId);
    }
}
