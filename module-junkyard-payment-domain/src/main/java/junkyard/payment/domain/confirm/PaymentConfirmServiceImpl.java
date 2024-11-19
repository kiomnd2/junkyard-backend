package junkyard.payment.domain.confirm;

import junkyard.common.response.exception.PaymentValidatorException;
import junkyard.common.response.exception.payment.NotFoundPaymentEventException;
import junkyard.common.response.exception.payment.NotFoundPaymentOrderException;
import junkyard.payment.domain.PaymentEvent;
import junkyard.payment.domain.PaymentEventReader;
import junkyard.payment.domain.order.PaymentOrder;
import junkyard.payment.domain.order.PaymentOrderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentConfirmServiceImpl implements PaymentConfirmService {
    private final PaymentOrderReader paymentOrderReader;
    private final PaymentEventReader paymentEventReader;
    private final PaymentValidator validator;

    @Transactional
    @Override
    public PaymentConfirmationResult confirm(PaymentConfirmCommand paymentConfirmCommand) {
        if (validator.isValid(paymentConfirmCommand.getOrderId(), paymentConfirmCommand.getAmount())) {
            paymentOrderReader.read(paymentConfirmCommand.getOrderId())
                    .forEach(PaymentOrder::changeOrderStatusToExecuting);

            Optional<PaymentEvent> optionalPaymentEvent = paymentEventReader.readByOrderId(paymentConfirmCommand.getOrderId());
            PaymentEvent event = optionalPaymentEvent
                    .orElseThrow(() -> new NotFoundPaymentEventException(paymentConfirmCommand.getOrderId()));

            event.updatePaymentKey(paymentConfirmCommand.getPaymentKey());
            return null;
        }
        throw new PaymentValidatorException(paymentConfirmCommand.getOrderId(), paymentConfirmCommand.getAmount());
    }
}
