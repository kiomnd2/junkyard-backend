package junkyard.payment.domain.confirm;

public interface PaymentConfirmService {
    void changeOrderStatusToExecuting(PaymentConfirmCommand paymentConfirmCommand);
    void changePaymentKey(String orderId, String paymentKey);
    void updateOrderStatus(PaymentStatusUpdateCommand statusUpdateCommand);
}
