package junkyard.checkout;

public interface CheckoutService {
    CheckoutResult checkout(Long authId, CheckoutCommand command);
}
