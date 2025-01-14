package junkyard.payment.domain.confirm;

import junkyard.payment.domain.PaymentEvent;

public interface PaymentConfirmService {
    void changeOrderStatusToExecuting(PaymentConfirmCommand paymentConfirmCommand);
    void changePaymentKey(String orderId, String paymentKey);
    PaymentEvent updateOrderStatus(PaymentStatusUpdateCommand statusUpdateCommand);
}
