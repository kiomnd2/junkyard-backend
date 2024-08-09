package junkyard.member.interfaces.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class KakaoMemberDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CheckoutResponse {
        private String callbackUrl;
        private String clientId;
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AccessTokenResponse {
        private String accessToken;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LoginResponse {
        private boolean isJoined; // 가입 여부
        private Long kakaoId;
        private String token;
        private String nickname;
    }
}
