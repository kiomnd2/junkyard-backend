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

    @GetMapping("/callback")
    public CommonResponse<KakaoMemberDto.AccessTokenResponse> loginCallback(@RequestParam(required = false) String code, @RequestParam(required = false) String state,
                                                                            @RequestParam(required = false) String error, @RequestParam(required = false) String error_description) {
        if (!Objects.isNull(error)) {
            // TODO Exception
        }

        return CommonResponse.success(KakaoMemberDto.AccessTokenResponse
                .builder()
                .accessToken(memberFacade.getAccessToken(code, "kakao"))
                .build());
    }

    @PostMapping("/auth-check")
    public CommonResponse<KakaoMemberDto.LoginResponse> authCheck(@RequestHeader String accessToken) {
        CheckUserResult checkMember = memberFacade.checkMember(accessToken);
        return CommonResponse.success(KakaoMemberDto.LoginResponse.builder()
                .kakaoId(checkMember.kakaoId())
                .token(checkMember.token())
                .isJoined(checkMember.isJoined())
                .nickname(checkMember.nickname())
                .build());
    }
}
