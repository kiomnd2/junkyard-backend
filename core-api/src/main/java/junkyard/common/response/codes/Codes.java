package junkyard.common.response.codes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Codes {
    NORMAL("정상처리되었습니다."),
    COMMON_SYSTEM_ERROR("시스템에 일시적으로 문제가 발생했습니다. 잠시 후 다시 시도해 주세요"),
    COMMON_REQUIRED_VALUE("매개변수 가 잘못되었습니다. 확인 바랍니다. (field : %s)"),
    COMMON_INVALID_TYPE_ERROR("찾을 수 없는 타입입니다. 확인 후 다시 시도해 주세요 (type : %s)");

    private final String description;

    public String getDescription(Object ... args) {
        return String.format(description, args);
    }
}
