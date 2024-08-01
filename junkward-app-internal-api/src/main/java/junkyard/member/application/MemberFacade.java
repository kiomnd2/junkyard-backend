package junkyard.member.application;

import junkyard.common.response.exception.InvalidTypeException;
import junkyard.member.domain.CheckUserResult;
import junkyard.member.domain.MemberService;
import junkyard.member.infrastructure.caller.AccessTokenCaller;
import junkyard.member.interfaces.MemberDto;
import junkyard.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class MemberFacade {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final Set<AccessTokenCaller> accessTokenCaller;

    public String joinMember(MemberDto.RequestJoin requestJoin) {
        Long authId = memberService.registerMember(requestJoin.toCommand());
        return jwtTokenProvider.createToken(authId);
    }

    public String getAccessToken(String code, String method) {
        for (AccessTokenCaller tokenCaller : accessTokenCaller) {
            if (tokenCaller.supports(method)) {
                return tokenCaller.trigger(code).accessToken();
            }
        }
        throw new InvalidTypeException(method);
    }

    public CheckUserResult checkMember(String accessToken) {

        return null;
    }
}
