package junkyard.common.response.exception;

import junkyard.common.response.codes.Codes;

public class InvalidTypeException extends BaseException {

    public InvalidTypeException(String type) {
        super(Codes.COMMON_INVALID_TYPE_ERROR, type);
    }
}
