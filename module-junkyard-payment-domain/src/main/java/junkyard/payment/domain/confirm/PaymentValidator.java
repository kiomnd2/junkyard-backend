package junkyard.payment.domain.confirm;

public interface PaymentValidator {
    boolean isValid(String orderId, Long amount);
}
