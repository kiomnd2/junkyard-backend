package junkyard.kakao.caller;

import junkyard.member.infrastructure.caller.UserInfoCaller;
import junkyard.member.infrastructure.caller.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class KakaoUserInfoCaller implements UserInfoCaller {
    private static final String KAKAO_USERINFO_REQUEST_URL = "https://kapi.kakao.com";

    @Override
    public boolean supports(String method) {
        return "kakao".equals(method);
    }

    @Override
    public UserInfoResponse call(String accessToken) {
        KakaoUserResponse response = WebClient.create(KAKAO_USERINFO_REQUEST_URL).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, "HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString()")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is3xxRedirection, clientResponse -> Mono.error(new RuntimeException("3xx error")))
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("4xx error")))
                .bodyToMono(KakaoUserResponse.class).block();

        if (response == null) {
            // TODO throw Exception
        }

        return UserInfoResponse.builder()
                .id(response.getId())
                .profileUrl(response.getKakaoAccount().getProfile().getProfileImageUrl())
                .nickname(response.getKakaoAccount().getProfile().getNickname())
                .build();
    }
}
