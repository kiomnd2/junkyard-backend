package junkyard.member.domain;

import java.util.Optional;

public interface MemberReader {
    Optional<MemberUser> readByAuthId(Long authId);
}
