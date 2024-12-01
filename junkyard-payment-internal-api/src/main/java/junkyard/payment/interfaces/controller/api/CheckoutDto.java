package junkyard.payment.interfaces.controller.api;

import jakarta.validation.constraints.NotNull;
import junkyard.payment.domain.checkout.CheckoutCommand;
import junkyard.payment.domain.checkout.CheckoutResult;
import junkyard.utils.IdempotencyCreator;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CheckoutDto {

    @Setter
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckoutRequest {

        @NotNull(message = "예약 ID 는 필수 값입니다.")
        private String reservationId;

        @NotNull(message = "구매자 ID 는 필수 값입니다.")
        private Long buyerId;

        public CheckoutCommand toCommand() {
            return CheckoutCommand.builder()
                    .idempotencyKey(IdempotencyCreator.create())
                    .reservationId(reservationId)
                    .buyerId(buyerId)
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckoutResponse {
        private String orderId;
        private String orderName;
        private BigDecimal amount;

        public static CheckoutResponse by(CheckoutResult checkout) {
            return CheckoutResponse.builder()
                    .orderId(checkout.getOrderId())
                    .orderName(checkout.getOrderName())
                    .amount(checkout.getAmount())
                    .build();
        }
    }
}
