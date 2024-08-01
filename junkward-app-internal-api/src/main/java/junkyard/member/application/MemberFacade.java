package junkyard.member.application;

import junkyard.kakao.caller.KakaoCaller;
import junkyard.kakao.caller.KakaoTokenResponse;
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
    private final KakaoCaller<String, KakaoTokenResponse> accessTokenCaller;

    public String joinMember(MemberDto.RequestJoin requestJoin) {
        Long authId = memberService.registerMember(requestJoin.toCommand());
        return jwtTokenProvider.createToken(authId);
    }

    public String getAccessToken(String code) {
        return accessTokenCaller.call(code).accessToken;
    }


}
