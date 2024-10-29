package junkyard.payment.domain;

public interface PaymentStore {
    PaymentEvent store(PaymentEvent paymentEvent);
}
