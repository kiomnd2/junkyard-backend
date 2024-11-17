package junkyard.common.response.exception.payment;

import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.BaseException;

public class NotFoundPaymentEventException extends BaseException {
    public NotFoundPaymentEventException(String orderId) {
        super(Codes.PAYMENT_STATUS_ERROR, "paymentEvent", orderId);

    }
}
