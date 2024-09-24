package junkyard.member.infrastructure.caller;

import lombok.Builder;

@Builder
public record AccessTokenResponse(String accessToken, String refreshToken) {
}
