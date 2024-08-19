package junkyard.member.exception;

import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.BaseException;
import lombok.Getter;

@Getter
public class InvalidUserException extends BaseException {
    private final Long userId;

    public InvalidUserException(Codes codes, Long userId, Object... args) {
        super(codes, userId);
        this.userId = userId;
    }
}
