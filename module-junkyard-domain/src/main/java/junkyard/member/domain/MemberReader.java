package junkyard.member.domain;

import java.util.Optional;

public interface MemberReader {
    Optional<MemberUser> readByAuthId(Long authId);
    MemberUser checkMember(Long authId);

    Optional<MemberAdmin> readAdminUser(String userId);
}
