package junkyard.payment.infrastructure.payment.order;

import junkyard.payment.domain.order.PaymentOrder;
import junkyard.payment.domain.order.PaymentOrderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PaymentOrderReaderImpl implements PaymentOrderReader {
    private final PaymentOrderRepository paymentOrderRepository;

    @Override
    public List<PaymentOrder> read(String orderId) {
        return paymentOrderRepository.readAllByOrderId(orderId);
    }
}
