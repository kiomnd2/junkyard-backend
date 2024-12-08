package junkyard.common.response.exception.member;

import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.BaseException;

public class InvalidAdminException extends BaseException {
    public InvalidAdminException(Codes codes, String userId) {
        super(codes, userId);
    }
}
