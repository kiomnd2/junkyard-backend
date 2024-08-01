package junkyard.common.response.exception;

import junkyard.common.response.codes.Codes;

public class BaseException extends RuntimeException {
    private Codes code;

    public BaseException(Codes codes, Object ... args) {
        super(codes.getDescription(args));
        this.code = codes;
    }
}
