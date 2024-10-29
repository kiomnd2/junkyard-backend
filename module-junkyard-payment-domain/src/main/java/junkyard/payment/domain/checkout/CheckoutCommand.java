package junkyard.payment.domain.checkout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CheckoutCommand {
    private String reservationId;
    private Long buyerId;
    private String idempotencyKey; // 물건에 대한 요청을 구분
}
