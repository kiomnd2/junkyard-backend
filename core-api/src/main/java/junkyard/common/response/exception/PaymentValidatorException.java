package junkyard.common.response.exception;

import junkyard.common.response.codes.Codes;

public class PaymentValidatorException extends BaseException {
    public PaymentValidatorException(String orderId, Long amount) {
        super(Codes.PAYMENT_VALID_ERROR, orderId, amount);
    }
}
