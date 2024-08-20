package junkyard.security;

import lombok.Builder;

@Builder
public record TokenClaim(String authId, String name, String profileUrl) {
}
