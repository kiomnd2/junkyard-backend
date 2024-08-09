package junkyard.member.interfaces.kakao;

import jakarta.validation.constraints.NotNull;
import junkyard.response.CommonResponse;
import junkyard.member.application.MemberFacade;
import junkyard.member.domain.CheckUserResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/member/kakao")
public class KakaoMemberApi {
    private final MemberFacade memberFacade;

    @Value("${kakao.api.client_id}")
    private String kakaoClientId;

    @Value("${kakao.api.callback_url}")
    private String callbackUrl;

    @PostMapping("/checkout")
    public CommonResponse<KakaoMemberDto.CheckoutResponse> checkout() {
        return CommonResponse.success(KakaoMemberDto.CheckoutResponse.builder()
                        .clientId(kakaoClientId)
                        .callbackUrl(callbackUrl)
                .build());
    }

    @PostMapping("/auth-check")
    public CommonResponse<KakaoMemberDto.LoginResponse> authCheck(@RequestHeader @NotNull String kakaoAccessToken) {
        CheckUserResult checkMember = memberFacade.checkMember(kakaoAccessToken, "kakao");
        return CommonResponse.success(KakaoMemberDto.LoginResponse.builder()
                .kakaoId(checkMember.authId())
                .token(checkMember.token())
                .isJoined(checkMember.isJoined())
                .nickname(checkMember.nickname())
                .build());
    }
}
