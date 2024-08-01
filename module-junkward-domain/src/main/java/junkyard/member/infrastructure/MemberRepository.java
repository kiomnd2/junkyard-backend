package junkyard.member.infrastructure;

import junkyard.member.domain.MemberUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberUser, Long> {
}
