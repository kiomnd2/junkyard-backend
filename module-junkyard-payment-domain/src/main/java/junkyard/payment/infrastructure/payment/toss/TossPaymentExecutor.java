package junkyard.payment.infrastructure.payment.toss;

import junkyard.payment.domain.*;
import junkyard.payment.infrastructure.payment.toss.exception.PSPConfirmationException;
import junkyard.payment.infrastructure.payment.toss.exception.TossPaymentError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class TossPaymentExecutor implements PaymentExecutor {
    private final WebClient webClient;
    private String url = "/v1/payment/confirm";

    @Override
    public PaymentExecutionResult execute(PaymentCommand.ConfirmCommand command) {
        String bodyValue = String.format("""
                {
                    "paymentKey": "%s",
                    "orderId": "%s",
                    "amount": "%s"
                }
                """, command.getPaymentKey(),
                        command.getOrderId(),
                        command.getAmount())
                .trim();
        TossPaymentConfirmationResponse response = webClient.post()
                .uri(url)
                .header("Idempotency-Key", command.getOrderId())
                .bodyValue(bodyValue)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, getClientResponseError())
                .onStatus(HttpStatusCode::is5xxServerError, getClientResponseError())
                .bodyToMono(TossPaymentConfirmationResponse.class)
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(1)).jitter(0.1)
                        .filter(throwable -> (throwable instanceof PSPConfirmationException &&
                                ((PSPConfirmationException)throwable).getIsRetryableError()))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                retrySignal.failure())
                )
                .block();
        return PaymentExecutionResult.builder()
                .paymentKey(command.getPaymentKey())
                .orderId(command.getOrderId())
                .paymentExtraDetails(PaymentExecutionResult.PaymentExtraDetails.builder()
                        .paymentType(PaymentEvent.PaymentType.get(response.getType()))
                        .paymentMethod(PaymentEvent.PaymentMethod.get(response.getMethod()))
                        .approvedAt(LocalDateTime.parse(response.getApprovedAt(), DateTimeFormatter.ISO_DATE_TIME))
                        .pspRawData(response.toString())
                        .orderName(response.getOrderName())
                        .pspConfirmationStatus(PSPConfirmationStatus.get(response.getStatus()))
                        .totalAmount((response.getTotalAmount()))
                        .build())
                .isSuccess(true)
                .isFailure(false)
                .isUnknown(false)
                .isRetryable(false)
                .build();
    }

    private static Function<ClientResponse, Mono<? extends Throwable>> getClientResponseError() {
        return clientResponse ->
                clientResponse.bodyToMono(TossPaymentConfirmationResponse.TossFailureResponse.class)
                        .flatMap(tossFailureResponse -> {
                            TossPaymentError tossPaymentError = TossPaymentError.get(tossFailureResponse.getCode());
                            return Mono.error(PSPConfirmationException.builder()
                                    .errorCode(tossPaymentError.name())
                                    .errorMessage(tossPaymentError.getDescription())
                                    .isSuccess(tossPaymentError.isSuccess())
                                    .isFailure(tossPaymentError.isFailure())
                                    .isUnknown(tossPaymentError.isUnknown())
                                    .isRetryableError(tossPaymentError.isRetryableError())
                                    .build());
                        });
    }
}
