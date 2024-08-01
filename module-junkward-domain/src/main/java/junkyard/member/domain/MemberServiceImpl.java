package junkyard.member.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberStore memberStore;

    @Transactional
    @Override
    public Long registerMember(MemberRegisterCommand registerCommand) {
        MemberUser memberUser = memberStore.storeMember(registerCommand.toEntity());
        return memberUser.getAuthId();
    }
}
