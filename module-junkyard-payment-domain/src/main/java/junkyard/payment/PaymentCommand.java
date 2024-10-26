package junkyard.payment;

import lombok.Builder;
import lombok.Getter;

public class PaymentCommand {

    @Builder
    @Getter
    public static class ConfirmCommand {
        private String paymentKey;
        private String orderId;
        private Long amount;
    }
}
