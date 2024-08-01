package junkyard.kakao.caller;

import junkyard.member.infrastructure.caller.AccessTokenCaller;
import junkyard.member.infrastructure.caller.AccessTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class KakaoAccessTokenCaller implements AccessTokenCaller {
    @Value("${kakao.api.client_id}")
    private String clientId;

    private static final String KAKAO_TOKEN_REQUEST_URL = "https://kauth.kakao.com";

    @Override
    public boolean supports(String method) {
        return "kakao".equals(method);
    }

    @Override
    public AccessTokenResponse trigger(String code) {
        KakaoTokenResponse tokenResponse = WebClient.create(KAKAO_TOKEN_REQUEST_URL).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, "HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString()")
                .retrieve()
                .onStatus(HttpStatusCode::is3xxRedirection, clientResponse -> Mono.error(new RuntimeException("3xx error")))
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("4xx error")))
                .bodyToMono(KakaoTokenResponse.class)
                .block();
        return Objects.requireNonNull(tokenResponse).toResponse();
    }
}
