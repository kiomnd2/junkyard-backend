package junkyard.member.application;

import junkyard.member.domain.MemberService;
import junkyard.member.interfaces.MemberDto;
import junkyard.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberFacade {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public String joinMember(MemberDto.RequestJoin requestJoin) {
        Long authId = memberService.registerMember(requestJoin.toCommand());
        return jwtTokenProvider.createToken(authId);
    }
}
