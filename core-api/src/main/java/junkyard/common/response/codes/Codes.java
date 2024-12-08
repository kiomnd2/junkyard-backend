package junkyard.common.response.codes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Codes {
    NORMAL("정상처리되었습니다."),
    COMMON_SYSTEM_ERROR("시스템에 일시적으로 문제가 발생했습니다. 잠시 후 다시 시도해 주세요"),
    COMMON_REQUIRED_VALUE("매개변수 가 잘못되었습니다. 확인 바랍니다. (field : %s)"),
    COMMON_INVALID_TYPE_ERROR("찾을 수 없는 타입입니다. 확인 후 다시 시도해 주세요 (type : %s)"),
    COMMON_ALREADY_JOIN_USER_ERROR("이미 가입된 사용자 입니다."),
    COMMON_INVALID_TOKEN_ERROR("유효하지 않은 토큰입니다, 확인 후 다시 시도해주세요."),
    COMMON_INVALID_MEMBER("찾을 수 없는 사용자 입니다. 확인 후 다시 시도해주세요. (userId : %d)"),
    COMMON_INVALID_ADMIN("찾을 수 없는 관리자 입니다. 확인 후 다시 시도해주세요. (userId : %s)"),
    COMMON_INVALID_PASSWORD_ADMIN("일치하지 않는 패스워드입니다. 확인 후 다시 시도해주세요. (userId : %s, password: %s)"),
    COMMON_INVALID_ERROR("잘못된 요청입니다. 확인 후 다시 시도해주세요. (type : %s)"),
    PAYMENT_STATUS_ERROR("결제할 수 있는 상태가 아닙니다. (status : %s, key: %s)"),
    PAYMENT_STATUS_SUCCESS_ERROR("이미 처리 성공한 결제 입니다. (status : %s)"),
    PAYMENT_STATUS_FAILED_ERROR("이미 처리 실패한 결제 입니다. (status : %s)"),
    ESTIMATE_INVALID_ERROR("잘못된 견적 내역입니다"),
    INVALID_RESERVATION("찾을 수 없는 예약입니다. 확인해주세요 (reservationId: %s, username: %s)"),
    INVALID_RESERVATION_CANCEL("올바르지 않은 취소요청입니다. [%s] (state : %s)"),
    TELEGRAM_ERROR("텔레그램 전송 중 오류가 발생했습니다. (chatId = %s, errorMessage: %s)"),
    PAYMENT_VALID_ERROR("결제 금액이 올바르지 않습니다 (orderId = %s, amount =%d");

    private final String description;

    public String getDescription(Object ... args) {
        return String.format(description, args);
    }
}
