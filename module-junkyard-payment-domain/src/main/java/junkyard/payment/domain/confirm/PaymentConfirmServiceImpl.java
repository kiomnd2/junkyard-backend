package junkyard.payment.domain.confirm;

import junkyard.payment.domain.order.PaymentOrder;
import junkyard.payment.domain.order.PaymentOrderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentConfirmServiceImpl implements PaymentConfirmService {
    private final PaymentConfirmStore paymentConfirmStore;
    private final PaymentOrderReader paymentOrderReader;

    @Override
    public PaymentConfirmationResult confirm(PaymentConfirmCommand paymentConfirmCommand) {
        Optional<PaymentOrder> paymentOrder = paymentOrderReader.read(paymentConfirmCommand.getOrderId());

        paymentConfirmStore.updatePaymentStatus(paymentConfirmCommand.getOrderId()
                , paymentConfirmCommand.getPaymentKey()
                , PaymentOrder.PaymentOrderStatus.EXECUTING);
        return null;
    }
}
