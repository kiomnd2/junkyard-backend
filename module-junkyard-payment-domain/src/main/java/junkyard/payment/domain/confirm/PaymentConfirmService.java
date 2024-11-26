package junkyard.payment.domain.confirm;

import junkyard.payment.domain.PaymentEvent;
import junkyard.payment.domain.order.PaymentOrder;

import java.util.List;

public interface PaymentConfirmService {
    void changeOrderStatusToExecuting(PaymentConfirmCommand paymentConfirmCommand);
    void changePaymentKey(String orderId, String paymentKey);
    void updateOrderStatus(PaymentStatusUpdateCommand statusUpdateCommand);
}
