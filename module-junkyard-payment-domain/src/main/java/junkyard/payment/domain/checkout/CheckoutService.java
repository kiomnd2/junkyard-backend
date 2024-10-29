package junkyard.payment.domain.checkout;

public interface CheckoutService {
    CheckoutResult checkout(Long authId, CheckoutCommand command);
}
