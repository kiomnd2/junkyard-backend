package junkyard.member.application;

import io.jsonwebtoken.security.InvalidKeyException;
import junkyard.common.response.exception.InvalidTypeException;
import junkyard.member.domain.CheckUserResult;
import junkyard.member.domain.MemberService;
import junkyard.member.infrastructure.caller.AccessTokenCaller;
import junkyard.member.infrastructure.caller.UserInfoCaller;
import junkyard.member.infrastructure.caller.UserInfoResponse;
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

    public TokenInfo joinMember(MemberDto.RequestJoin requestJoin) {
        Long authId = memberService.registerMember(requestJoin.toCommand());
        String token = jwtTokenProvider.createToken(authId);
        String refreshToken = jwtTokenProvider.createRefreshToken(authId);
        return TokenInfo.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();

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
                UserInfoResponse call = caller.call(accessToken);
                if (memberService.checkMember(call.id())) {
                    return CheckUserResult.builder()
                            .isJoined(true)
                            .authId(call.id())
                            .accessToken(jwtTokenProvider.createToken(call.id()))
                            .refreshToken(jwtTokenProvider.createRefreshToken(call.id()))
                            .nickname(call.nickname())
                            .build();
                }
                return CheckUserResult.builder()
                        .isJoined(false)
                        .authId(call.id())
                        .build();
            }
        }
        throw new InvalidTypeException(method);
    }

    public TokenInfo refresh(String refreshToken) {
        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            String userId = jwtTokenProvider.getRefreshSubject(refreshToken);
            String accessToken = jwtTokenProvider.createToken(Long.parseLong(userId));
            return TokenInfo.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new InvalidKeyException("유효하지 않은 refresh Token 입니다. " + refreshToken);
    }
}
