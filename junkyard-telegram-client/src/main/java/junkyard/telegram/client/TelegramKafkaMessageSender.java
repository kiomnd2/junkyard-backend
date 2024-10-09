package junkyard.telegram.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TelegramKafkaMessageSender implements TelegramMessageSender {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.alarm.telegram.topic}")
    private String topic;

    @Override
    public void sendMessage(String message, Object ... args) {
        String formattedMessage = String.format(message, args);
        System.out.println("producer Message : " + formattedMessage);
        this.kafkaTemplate.send(topic, formattedMessage);
    }
}
