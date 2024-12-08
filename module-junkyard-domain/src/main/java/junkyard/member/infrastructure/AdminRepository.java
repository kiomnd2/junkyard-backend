package junkyard.member.infrastructure;

import junkyard.member.domain.MemberAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<MemberAdmin, Long> {
    Optional<MemberAdmin> findByLoginId(String loginId);
}
