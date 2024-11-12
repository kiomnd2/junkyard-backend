package junkyard.payment.domain.confirm;

import junkyard.payment.domain.order.PaymentOrder;

public interface PaymentConfirmStore {

    void updatePaymentStatus(String orderId, String paymentKey, PaymentOrder.PaymentOrderStatus paymentOrderStatus);
}
