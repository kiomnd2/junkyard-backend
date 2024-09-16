package junkyard.telegram.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class AlarmCommand {

    @Builder
    @Getter
    public static class RequestAlarm {
        private final String title;
        private final String message;
        private final String writer;
        private final LocalDateTime sendAt;
    }
}
