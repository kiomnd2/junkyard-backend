package junkyard.telegram.infrastructure;

import junkyard.telegram.domain.AlarmCommand;

public interface AlarmCaller {
    boolean supports(AlarmType type);
    void sendMessage(AlarmCommand.RequestAlarm message);
}
