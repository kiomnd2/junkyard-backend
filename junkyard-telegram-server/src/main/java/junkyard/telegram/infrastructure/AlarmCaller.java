package junkyard.telegram.infrastructure;

import junkyard.telegram.domain.AlarmCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface AlarmCaller {
    boolean supports(AlarmType type);
    void sendMessage(AlarmCommand.RequestAlarm message);
}
