package junkyard.telegram.interfaces.handler;

import junkyard.telegram.domain.AlarmCommand;
import junkyard.telegram.domain.AlarmSender;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class AlarmConsumer {
    private final AlarmSender alarmSender;

    @KafkaListener(topics = "${kafka.consumer.topic}", groupId = "junkyard-group", containerFactory = "kafkaListenerContainerFactory")
    public String listenAlarm(ConsumerRecord<?,?> record) {
        AlarmCommand.RequestAlarm message = AlarmCommand.RequestAlarm.builder()
                .title("익차장 Alarm")
                .message(record.value().toString())
                .sendAt(LocalDateTime.now())
                .writer("junkyard-alarm")
                .build();
        alarmSender.sendMessage("KAKAO", message);
        return "ok";
    }
}
