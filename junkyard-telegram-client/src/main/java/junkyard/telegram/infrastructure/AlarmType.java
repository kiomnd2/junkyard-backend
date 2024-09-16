package junkyard.telegram.infrastructure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmType {
    KAKAO("카카오"),TELEGRAM("텔레그램");

    private final String description;
}
