package junkyard.telegram.infrastructure.telegram;

import junkyard.telegram.domain.AlarmCommand;
import junkyard.telegram.infrastructure.AlarmCaller;
import junkyard.telegram.infrastructure.AlarmType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class TelegramAlarmCaller implements AlarmCaller {
    private final RestTemplate restTemplate;
    private final TelegramProperties telegramProperties;
    @Override
    public boolean supports(AlarmType type) {
        return AlarmType.TELEGRAM == type;
    }

    @Override
    public void sendMessage(AlarmCommand.RequestAlarm command) {
        TelegramMessageCommand message = TelegramMessageCommand.builder()
                .chatId(telegramProperties.getChatId())
                .message(command.getMessage())
                .build();

        HttpEntity<String> req = new HttpEntity<>(command.toString());
        String s = restTemplate.postForObject(telegramProperties.getUrl()+"/bot"+ telegramProperties.getToken() +"/sendMessage", req, String.class);
    }
}
