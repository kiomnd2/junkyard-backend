package junkyard.member.interfaces.kakao;

import jakarta.validation.constraints.NotNull;
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
