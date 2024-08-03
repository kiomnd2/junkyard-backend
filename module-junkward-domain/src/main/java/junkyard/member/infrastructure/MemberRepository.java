package junkyard.member.infrastructure;

import junkyard.member.domain.MemberUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberUser, Long> {
    Optional<MemberUser> findByAuthId(Long authId);
}
