package junkyard.member.domain;

import lombok.Builder;

@Builder
public record MemberInfo(Long authId, String name, String profileUrl, String phoneNo, String email) {
}
