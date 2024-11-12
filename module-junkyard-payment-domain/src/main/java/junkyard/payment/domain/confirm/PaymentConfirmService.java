package junkyard.payment.domain.confirm;

public interface PaymentConfirmService {
    PaymentConfirmationResult confirm(PaymentConfirmCommand paymentConfirmCommand);

}
