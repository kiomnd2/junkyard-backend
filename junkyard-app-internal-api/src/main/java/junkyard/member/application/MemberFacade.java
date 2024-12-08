package junkyard.member.application;

import io.jsonwebtoken.security.InvalidKeyException;
import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.InvalidTypeException;
import junkyard.common.response.exception.member.InvalidUserException;
import junkyard.member.domain.CheckUserResult;
import junkyard.member.domain.MemberCommand;
import junkyard.member.domain.MemberInfo;
import junkyard.member.domain.MemberService;
import junkyard.member.infrastructure.caller.AccessTokenCaller;
import junkyard.member.infrastructure.caller.UserInfoCaller;
import junkyard.member.infrastructure.caller.UserInfoResponse;
import junkyard.member.interfaces.MemberDto;
import junkyard.security.JwtTokenProvider;
import junkyard.security.TokenClaim;
import junkyard.security.userdetails.UserRoles;
import junkyard.telegram.client.TelegramMessageSender;
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
    private final TelegramMessageSender messageSender;

    public TokenInfo joinMember(MemberDto.RequestJoin requestJoin) {
        MemberInfo memberInfo = memberService.registerMember(requestJoin.toCommand());
        String token = jwtTokenProvider.createToken(memberInfo.authId(), memberInfo.name(), memberInfo.profileUrl(), UserRoles.USER);
        String refreshToken = jwtTokenProvider.createRefreshToken(memberInfo.authId(), memberInfo.name(), memberInfo.profileUrl(), UserRoles.USER);
        messageSender.sendMessage("[%s] 님이 가입했습니다.", memberInfo.name());
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
        for (UserInfoCaller caller: userInfoCallers) {
            if (caller.supports(method)) {
                UserInfoResponse call = caller.call(accessToken);
                if (memberService.checkMember(call.id())) {
                    MemberInfo member = memberService.findMember(call.id());
                    return CheckUserResult.builder()
                            .isJoined(true)
                            .authId(call.id())
                            .accessToken(jwtTokenProvider.createToken(call.id(), member.name(), member.profileUrl(), UserRoles.USER))
                            .refreshToken(jwtTokenProvider.createRefreshToken(call.id(), member.name(), member.profileUrl(), UserRoles.USER))
                            .nickname(call.nickname())
                            .build();
                }
                return CheckUserResult.builder()
                        .isJoined(false)
                        .authId(call.id())
                        .nickname(call.nickname())
                        .profileUrl(call.profileUrl())
                        .build();
            }
        }
        throw new InvalidTypeException(method);
    }

    public TokenInfo refresh(String refreshToken) {
        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            TokenClaim refreshSubject = jwtTokenProvider.getRefreshSubject(refreshToken);
            String accessToken = jwtTokenProvider.createToken(Long.parseLong(refreshSubject.authId()),
                    refreshSubject.name(), refreshSubject.profileUrl(), UserRoles.USER);
            CheckUserResult checkUserResult = checkMember(accessToken, "kakao");

            if (!checkUserResult.isJoined()) {
                throw new InvalidUserException(Codes.COMMON_INVALID_MEMBER, null);
            }

            return TokenInfo.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new InvalidKeyException("유효하지 않은 refresh Token 입니다. " + refreshToken);
    }

    public TokenInfo loginAdmin(MemberCommand.AdminLoginCommand command) {
        memberService.checkAdmin(command);
        String accessToken = jwtTokenProvider.createToken(1L, "Admin", "", UserRoles.ADMIN);
        String refreshToken = jwtTokenProvider.createRefreshToken(1L, "Admin", "", UserRoles.ADMIN);

        return TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
