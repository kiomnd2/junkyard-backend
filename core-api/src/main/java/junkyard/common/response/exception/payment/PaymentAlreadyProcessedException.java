package junkyard.common.response.exception.payment;

import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.BaseException;

public class PaymentAlreadyProcessedException extends BaseException {

    public PaymentAlreadyProcessedException(Codes codes, String status) {
        super(codes, status);
    }
}
