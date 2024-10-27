package junkyard.common.response.exception.payment;

import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.BaseException;

public class CheckoutStatusException extends BaseException {
    public CheckoutStatusException(String status) {
        super(Codes.COMMON_INVALID_MEMBER, status);
    }
}
