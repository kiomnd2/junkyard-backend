package junkyard.member.application;

import junkyard.member.interfaces.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberFacade {

    public String joinMember(MemberDto.RequestJoin requestJoin) {
        return null;
    }
}
