package junkyard.payment.infrastructure.payment;

import junkyard.payment.domain.PaymentEvent;
import junkyard.payment.domain.PaymentStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentStoreImpl implements PaymentStore {
    private final PaymentEventRepository paymentEventRepository;

    @Override
    public PaymentEvent store(PaymentEvent paymentEvent) {
        return paymentEventRepository.save(paymentEvent);
    }
}
