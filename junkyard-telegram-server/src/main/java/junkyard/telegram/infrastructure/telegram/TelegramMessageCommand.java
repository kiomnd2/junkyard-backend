package junkyard.telegram.infrastructure.telegram;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TelegramMessageCommand {
    private String chatId;
    private String message;
}
