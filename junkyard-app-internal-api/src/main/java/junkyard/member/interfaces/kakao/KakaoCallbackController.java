package junkyard.member.interfaces.kakao;

import junkyard.member.application.MemberFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@RequiredArgsConstructor
@Controller
@RequestMapping("kakao")
public class KakaoCallbackController {
    private final MemberFacade memberFacade;

    @Value("${front.kakao.redirect.url}")
    private String redirctUrl;

    @GetMapping("/callback")
    public String loginCallback(@RequestParam(required = false) String code,
                                @RequestParam(required = false) String state,
                                @RequestParam(required = false) String error,
                                @RequestParam(required = false) String error_description) {

        if (!Objects.isNull(error)) {
            // TODO Exception
        }

        String accessToken = memberFacade.getAccessToken(code, "kakao");
        return "redirect:" + redirctUrl + "?accessToken=" + accessToken;
    }
}
