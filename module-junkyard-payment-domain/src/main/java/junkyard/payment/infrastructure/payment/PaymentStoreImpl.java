package junkyard.payment.infrastructure.payment;

import junkyard.payment.domain.PaymentEvent;
import junkyard.payment.domain.PaymentStore;
import junkyard.payment.domain.confirm.PaymentConfirmStore;
import junkyard.payment.domain.order.PaymentOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentStoreImpl implements PaymentStore, PaymentConfirmStore {
    private final PaymentEventRepository paymentEventRepository;

    @Override
    public PaymentEvent store(PaymentEvent paymentEvent) {
        return paymentEventRepository.save(paymentEvent);
    }

    @Override
    public void updatePaymentStatus(String orderId, String paymentKey, PaymentOrder.PaymentOrderStatus paymentOrderStatus) {
        
    }
}
