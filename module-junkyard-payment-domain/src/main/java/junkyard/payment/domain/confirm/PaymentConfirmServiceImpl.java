package junkyard.payment.domain.confirm;

import junkyard.common.response.exception.PaymentValidatorException;
import junkyard.common.response.exception.payment.NotFoundPaymentEventException;
import junkyard.common.response.exception.payment.NotFoundPaymentOrderException;
import junkyard.payment.domain.*;
import junkyard.payment.domain.order.PaymentOrder;
import junkyard.payment.domain.order.PaymentOrderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentConfirmServiceImpl implements PaymentConfirmService {
    private final PaymentOrderReader paymentOrderReader;
    private final PaymentEventReader paymentEventReader;

    @Transactional
    public void changeOrderStatusToExecuting(PaymentConfirmCommand paymentConfirmCommand) {
        List<PaymentOrder> orders = paymentOrderReader.read(paymentConfirmCommand.getOrderId());
        orders.forEach(PaymentOrder::changeOrderStatusToExecuting);
    }

    @Transactional
    public void changePaymentKey(String orderId, String paymentKey) {
        Optional<PaymentEvent> optionalPaymentEvent = paymentEventReader.readByOrderId(orderId);
        PaymentEvent event = optionalPaymentEvent
                .orElseThrow(() -> new NotFoundPaymentEventException(orderId));

        event.updatePaymentKey(paymentKey);
    }

    @Transactional
    public void updateOrderStatus(PaymentStatusUpdateCommand statusUpdateCommand) {
        List<PaymentOrder> paymentOrders = paymentOrderReader.read(statusUpdateCommand.getOrderId());
        Optional<PaymentEvent> optionalPaymentEvent = paymentEventReader.readByOrderId(statusUpdateCommand.getOrderId());
        PaymentEvent event = optionalPaymentEvent
                .orElseThrow(() -> new NotFoundPaymentEventException(statusUpdateCommand.getOrderId()));

        for (PaymentOrder order: paymentOrders) {
            order.changeUpdateStatus(statusUpdateCommand.getStatus());
        }

        event.updateExtraDetail(statusUpdateCommand);
    }


}
