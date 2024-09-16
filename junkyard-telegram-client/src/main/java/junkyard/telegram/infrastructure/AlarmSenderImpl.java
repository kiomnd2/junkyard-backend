package junkyard.telegram.infrastructure;

import junkyard.telegram.domain.AlarmCommand;
import junkyard.telegram.domain.AlarmSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AlarmSenderImpl implements AlarmSender {
    private final AlarmCaller alarmCaller;
    @Override
    public void sendMessage(AlarmCommand.RequestAlarm alarm) {
        alarmCaller.sendMessage(alarm);
    }
}
