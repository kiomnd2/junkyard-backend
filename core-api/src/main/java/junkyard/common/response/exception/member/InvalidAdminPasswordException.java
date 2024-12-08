package junkyard.common.response.exception.member;

import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.BaseException;

public class InvalidAdminPasswordException extends BaseException {
    public InvalidAdminPasswordException(Codes codes, String userId, String password) {
        super(codes, userId);
    }

}
