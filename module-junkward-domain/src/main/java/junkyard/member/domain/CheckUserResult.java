package junkyard.member.domain;

import lombok.Builder;

public record CheckUserResult(Boolean isJoined, Long authId, String accessToken, String refreshToken, String nickname) {

    @Builder
    public CheckUserResult {
        if (isJoined && (authId == null || accessToken == null)) {
            throw new RuntimeException(String.format("정상 가입상태 시 kakaoID 와 accesstoken 은 필수입니다. " +
                    "(kakaoID : %s, accessToken :%s", authId, accessToken));
        }

        if (!isJoined && authId == null) {
            throw new RuntimeException(String.format("미가입 상태 시 kakaoID 는 필수입니다. " +
                    "(kakaoID : %s) ", authId));
        }
    }
}
