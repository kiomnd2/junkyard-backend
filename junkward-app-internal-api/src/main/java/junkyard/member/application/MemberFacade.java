package junkyard.member.application;

import junkyard.common.response.exception.InvalidTypeException;
import junkyard.member.domain.CheckUserResult;
import junkyard.member.domain.MemberService;
import junkyard.member.infrastructure.caller.AccessTokenCaller;
import junkyard.member.infrastructure.caller.UserInfoCaller;
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
    private final Set<UserInfoCaller> userInfoCallers;

    public String joinMember(MemberDto.RequestJoin requestJoin) {
        Long authId = memberService.registerMember(requestJoin.toCommand());
        return jwtTokenProvider.createToken(authId);
    }

    public String getAccessToken(String code, String method) {
        for (AccessTokenCaller tokenCaller : accessTokenCaller) {
            if (tokenCaller.supports(method)) {
                return tokenCaller.call(code).accessToken();
            }
        }
        throw new InvalidTypeException(method);
    }

    public CheckUserResult checkMember(String accessToken, String method) {
        for (UserInfoCaller caller: userInfoCallers ) {
            if (caller.supports(method)) {
                caller.call(accessToken);
            }
        }
        throw new InvalidTypeException(method);
    }
}
