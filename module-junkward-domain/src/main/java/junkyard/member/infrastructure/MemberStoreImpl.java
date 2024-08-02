package junkyard.member.infrastructure;

import junkyard.member.domain.MemberStore;
import junkyard.member.domain.MemberUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Member;

@RequiredArgsConstructor
@Component
public class MemberStoreImpl implements MemberStore {
    private final MemberRepository memberRepository;

    @Override
    public MemberUser storeMember(MemberUser member) {
        //test
        return memberRepository.save(member);
    }
}
