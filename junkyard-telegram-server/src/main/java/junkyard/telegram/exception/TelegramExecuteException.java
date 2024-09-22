package junkyard.telegram.exception;

import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.BaseException;

public class TelegramExecuteException extends BaseException {
    public TelegramExecuteException(Exception e, String chatId) {
        super(Codes.TELEGRAM_ERROR, chatId, e.getMessage());
    }
}
