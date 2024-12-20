package junkyard.payment.interfaces.controller.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class TossPaymentDto {

    @Getter
    @Setter
    @ToString
    public static class TossPaymentConfirmRequest {
        private String paymentKey;
        private String orderId;
        private String amount;
    }
}
