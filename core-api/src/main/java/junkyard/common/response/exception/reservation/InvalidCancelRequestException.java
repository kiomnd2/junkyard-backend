package junkyard.common.response.exception.reservation;

import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.BaseException;

public class InvalidCancelRequestException extends BaseException {
    public InvalidCancelRequestException(Codes codes, Object... args) {
        super(codes, args);
    }
}
