package junkyard.member.infrastructure.caller;


import lombok.Builder;

@Builder
public record UserInfoResponse(Long id, String nickname, String profileUrl) { }

