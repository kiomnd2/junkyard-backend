package junkyard.payment.infrastructure.payment.toss;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.Base64;
import java.util.List;

@TestConfiguration
public class PSPTestWebClientConfiguration {
    @Value("${psp.toss.url}") private String baseUrl;
    @Value("${psp.toss.secretKey}") private String secretKey;

    public WebClient createTestTossWebClient(List<Pair<String, String>> customerHeaderKeyValue) {
        String encodedSecretKey = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedSecretKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeaders(httpHeaders -> customerHeaderKeyValue.forEach(stringStringPair
                        -> httpHeaders.add(stringStringPair.getFirst(), stringStringPair.getSecond())))
                .clientConnector(reactorClientHttpConnector())
                .build();
    }

    private ClientHttpConnector reactorClientHttpConnector() {
        return new ReactorClientHttpConnector(HttpClient
                .create(ConnectionProvider.builder("test-toss-payment")
                .build()));
    }

}
