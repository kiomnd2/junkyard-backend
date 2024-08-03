package junkyard.member.interfaces.kakao;

import junkyard.common.response.CommonResponse;
import junkyard.member.application.MemberFacade;
import junkyard.member.domain.CheckUserResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/member/kakao")
public class KakaoMemberApi {
    private final MemberFacade memberFacade;



    @PostMapping("/auth-check")
    public CommonResponse<KakaoMemberDto.LoginResponse> authCheck(@RequestHeader String accessToken) {
        CheckUserResult checkMember = memberFacade.checkMember(accessToken, "kakao");
        return CommonResponse.success(KakaoMemberDto.LoginResponse.builder()
                .kakaoId(checkMember.authId())
                .token(checkMember.token())
                .isJoined(checkMember.isJoined())
                .nickname(checkMember.nickname())
                .build());
    }
}
