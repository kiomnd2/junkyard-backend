package junkyard.common.response.exception.reservation;

import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.BaseException;

public class InvalidEstimateException extends BaseException {
    public InvalidEstimateException() {
        super(Codes.ESTIMATE_INVALID_ERROR);
    }
}
