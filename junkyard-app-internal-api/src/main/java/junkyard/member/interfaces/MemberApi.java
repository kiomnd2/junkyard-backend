package junkyard.member.interfaces;

import junkyard.member.application.TokenInfo;
import junkyard.response.CommonResponse;
import junkyard.member.application.MemberFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/member")
public class MemberApi {
    private final MemberFacade memberFacade;

    @PostMapping("/join")
    public CommonResponse<MemberDto.ResponseJoin> join(@RequestBody MemberDto.RequestJoin requestJoin) {
        TokenInfo token = memberFacade.joinMember(requestJoin);
        return CommonResponse.success(MemberDto.ResponseJoin.builder()
                        .token(token)
                .build());
    }

    @PostMapping("/login")
    public CommonResponse<MemberDto.ResponseJoin> login(@RequestBody MemberDto.RequestAdminLogin requestLogin) {
        TokenInfo tokenInfo = memberFacade.loginAdmin(requestLogin.toCommand());
        return CommonResponse.success(MemberDto.ResponseJoin.builder()
                        .token(tokenInfo)
                .build());
    }

    // 어드민용 로그인

    @PostMapping("/refresh-token")
    public CommonResponse<MemberDto.ResponseRefresh> refreshToken(@RequestHeader String refreshToken) {
        TokenInfo tokenInfo = memberFacade.refresh(refreshToken);
        return CommonResponse.success(MemberDto.ResponseRefresh.builder()
                        .token(tokenInfo)
                .build());
    }
}
