package junkyard.telegram.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Component
public class TelegramKafkaMessageSender implements TelegramMessageSender {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.alarm.telegram.topic}")
    private String topic;

    @Override
    public void sendMessage(String message) {
        System.out.println("producer Message : " + message);
        this.kafkaTemplate.send(topic, message);
    }
}
