package junkyard.common.response.exception;

import junkyard.common.response.codes.Codes;

public class AlreadyExistUserException extends BaseException {
    public AlreadyExistUserException() {
        super(Codes.COMMON_ALREADY_JOIN_USER_ERROR);
    }
}
