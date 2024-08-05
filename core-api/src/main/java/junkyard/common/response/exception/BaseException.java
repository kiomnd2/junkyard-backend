package junkyard.common.response.exception;

import junkyard.common.response.codes.Codes;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final Codes code;
    private final Object[] args;

    public BaseException(Codes codes, Object ... args) {
        super(codes.getDescription(args));
        this.code = codes;
        this.args = args;
    }
}
