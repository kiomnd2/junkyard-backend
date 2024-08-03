package junkyard.member.infrastructure;

import junkyard.member.domain.MemberReader;
import junkyard.member.domain.MemberUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Member;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class MemberReaderImpl implements MemberReader {
    private final MemberRepository memberRepository;

    @Override
    public Optional<MemberUser> readByAuthId(Long authId) {
        return memberRepository.findByAuthId(authId);
    }
}