package junkyard.payment.domain.confirm;

import junkyard.common.response.exception.payment.NotFoundPaymentEventException;
import junkyard.payment.domain.PaymentEvent;
import junkyard.payment.domain.PaymentEventReader;
import junkyard.payment.domain.confirm.PaymentValidator;
import junkyard.payment.domain.order.PaymentOrder;
import junkyard.payment.domain.order.PaymentOrderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PaymentValidatorImpl implements PaymentValidator {
    private final PaymentEventReader paymentEventReader;


    @Override
    public boolean isValid(String orderId, Long amount) {
        PaymentEvent event = paymentEventReader.readByOrderId(orderId)
                .orElseThrow(() -> new NotFoundPaymentEventException(orderId));
        Long totalAmount = event.totalAmount();
        return Objects.equals(totalAmount, amount);
    }
}
