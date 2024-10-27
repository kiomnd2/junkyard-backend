package junkyard.checkout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CheckoutResult {
    private BigDecimal amount;
    private String orderId;
    private String orderName;
}
