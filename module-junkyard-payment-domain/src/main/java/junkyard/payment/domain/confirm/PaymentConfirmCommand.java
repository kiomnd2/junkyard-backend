package junkyard.payment.domain.confirm;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentConfirmCommand {
    private String paymentKey;
    private String orderId;
    private Long amount;
}
