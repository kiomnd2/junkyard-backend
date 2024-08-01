package junkyard.member.domain;

import lombok.Builder;

public record CheckUserResult(Boolean isJoined, Long kakaoId, String token, String nickname) {

    @Builder
    public CheckUserResult {
        if (isJoined && (kakaoId == null || token == null)) {
            throw new RuntimeException(String.format("정상 가입상태 시 kakaoID 와 accesstoken 은 필수입니다. " +
                    "(kakaoID : %s, accessToken :%s", kakaoId, token));
        }

        if (!isJoined && kakaoId == null) {
            throw new RuntimeException(String.format("미가입 상태 시 kakaoID 는 필수입니다. " +
                    "(kakaoID : %s) ", kakaoId));
        }
    }
}
