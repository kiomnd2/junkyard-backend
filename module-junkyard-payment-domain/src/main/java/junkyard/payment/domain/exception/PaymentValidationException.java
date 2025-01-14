package junkyard.payment.domain.exception;

import lombok.Getter;

@Getter
public class PaymentValidationException extends RuntimeException {
    private String message;

    public PaymentValidationException(String message) {
        super(message);
        this.message = message;
    }
}

