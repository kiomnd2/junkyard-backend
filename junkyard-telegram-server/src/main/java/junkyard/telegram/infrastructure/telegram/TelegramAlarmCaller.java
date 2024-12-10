package junkyard.telegram.infrastructure.telegram;

import jakarta.annotation.PostConstruct;
import junkyard.telegram.domain.AlarmCommand;
import junkyard.telegram.exception.TelegramExecuteException;
import junkyard.telegram.infrastructure.AlarmCaller;
import junkyard.telegram.infrastructure.AlarmType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@RequiredArgsConstructor
@Component
public class TelegramAlarmCaller extends TelegramLongPollingBot implements AlarmCaller {
    private final TelegramProperties telegramProperties;

    @PostConstruct
    public void init() throws Exception {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(this);
    }

    @Override
    public boolean supports(AlarmType type) {
        return AlarmType.TELEGRAM == type;
    }

    @Override
    public void sendMessage(AlarmCommand.RequestAlarm command) {
        SendMessage message = SendMessage.builder()
                .chatId(telegramProperties.getChatId())
                .text(command.getMessage())
                .build();
        try {
            execute(message);
        } catch (Exception e) {
            throw new TelegramExecuteException(e, telegramProperties.getChatId());
        }
    }

    @Override
    public String getBotToken() {
        return telegramProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
    }

    @Override
    public String getBotUsername() {
        return telegramProperties.getChatId();
    }
}
