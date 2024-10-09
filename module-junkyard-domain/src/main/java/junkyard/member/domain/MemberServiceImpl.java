package junkyard.member.domain;

import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.AlreadyExistUserException;
import junkyard.common.response.exception.member.InvalidUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberStore memberStore;
    private final MemberReader memberReader;

    @Transactional
    @Override
    public MemberInfo registerMember(MemberRegisterCommand registerCommand) {
        if (memberReader.readByAuthId(registerCommand.id()).isPresent()) {
            throw new AlreadyExistUserException();
        }

        MemberUser memberUser = memberStore.storeMember(registerCommand.toEntity());
        return memberUser.toInfo();
    }

    @Override
    public boolean checkMember(Long authId) {
        return memberReader.readByAuthId(authId).isPresent();
    }

    @Override
    public MemberInfo findMember(Long authId) {
        return memberReader.readByAuthId(authId)
                .orElseThrow(() -> new InvalidUserException(Codes.COMMON_INVALID_MEMBER, authId)).toInfo();
    }
}
