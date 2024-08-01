package junkyard.member.interfaces;

import junkyard.common.response.CommonResponse;
import junkyard.member.application.MemberFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/member")
public class MemberApi {
    private final MemberFacade memberFacade;

    @PostMapping("/join")
    public CommonResponse<MemberDto.ResponseJoin> join(@RequestBody MemberDto.RequestJoin requestJoin) {
        String token = memberFacade.joinMember(requestJoin);
        return CommonResponse.success(MemberDto.ResponseJoin.builder()
                        .token(token)
                .build());
    }
}
