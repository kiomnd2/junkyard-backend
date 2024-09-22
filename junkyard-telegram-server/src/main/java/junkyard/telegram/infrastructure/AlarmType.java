package junkyard.telegram.infrastructure;

import junkyard.common.response.exception.InvalidTypeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmType {
    KAKAO("카카오"),TELEGRAM("텔레그램");

    private final String description;

    public static AlarmType get(String method) {
        AlarmType[] values = values();
        for (AlarmType value : values) {
            if (method.equals(value.name())) {
                return value;
            }
        }
        throw new InvalidTypeException(method);
    }
}
