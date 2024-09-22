package junkyard.telegram.infrastructure;

import junkyard.telegram.domain.AlarmCommand;
import junkyard.telegram.domain.AlarmSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AlarmSenderImpl implements AlarmSender {
    private final List<AlarmCaller> alarmCaller;
    @Override
    public void sendMessage(String method, AlarmCommand.RequestAlarm alarm) {
        for (AlarmCaller caller : alarmCaller) {
            if (caller.supports(AlarmType.get(method))) {
                caller.sendMessage(alarm);
            }
        }
    }
}
