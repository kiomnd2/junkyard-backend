package junkyard.payment.infrastructure.payment.toss;

import junkyard.domain.payment.*;
import junkyard.payment.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
                .bodyToMono(TossPaymentConfirmationResponse.class)
                .block();
        ;
        return PaymentExecutionResult.builder()
                .paymentKey(response.getPaymentKey())
                .orderId(response.getOrderId())
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
}
