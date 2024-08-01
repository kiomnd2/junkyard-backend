package junkyard.common.response.codes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Codes {
    NORMAL("정상처리되었습니다."),
    COMMON_SYSTEM_ERROR("시스템에 일시적으로 문제가 발생했습니다. 잠시 후 다시 시도해 주세요");

    private final String description;

}
