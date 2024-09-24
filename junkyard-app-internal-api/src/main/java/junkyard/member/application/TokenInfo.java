package junkyard.member.application;

import lombok.Builder;

@Builder
public record TokenInfo(String accessToken, String refreshToken) {
}
