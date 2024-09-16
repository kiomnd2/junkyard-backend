package junkyard.telegram.infrastructure.telegram;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "alarm.telegram")
public class TelegramProperties {
    private String url;
    private String chatId;
    private String token;
}
