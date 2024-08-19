package junkyard.member.infrastructure;

import junkyard.common.response.codes.Codes;
import junkyard.member.domain.MemberReader;
import junkyard.member.domain.MemberUser;
import junkyard.common.response.exception.member.InvalidUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class MemberReaderImpl implements MemberReader {
    private final MemberRepository memberRepository;

    @Override
    public Optional<MemberUser> readByAuthId(Long authId) {
        return memberRepository.findByAuthId(authId);
    }

    @Override
    public MemberUser checkMember(Long authId) {
        return readByAuthId(authId).orElseThrow(() -> new InvalidUserException(Codes.COMMON_INVALID_MEMBER, authId));
    }
}
