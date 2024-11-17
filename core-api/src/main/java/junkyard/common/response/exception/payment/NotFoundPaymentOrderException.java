package junkyard.common.response.exception.payment;

import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.BaseException;

public class NotFoundPaymentOrderException extends BaseException {
    public NotFoundPaymentOrderException(String key) {
        super(Codes.PAYMENT_STATUS_ERROR, "paymentOrder", key);
    }
}
