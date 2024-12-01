package junkyard.payment.interfaces.controller.api;

import junkyard.payment.application.ConfirmFacade;
import junkyard.payment.domain.confirm.PaymentConfirmCommand;
import junkyard.payment.domain.confirm.PaymentConfirmationResult;
import junkyard.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/v1/payment/toss")
public class TossPaymentApi {
    private final ConfirmFacade confirmFacade;

    @PostMapping("/confirm")
    public ResponseEntity<CommonResponse<PaymentConfirmationResult>> confirm(@RequestBody TossPaymentDto.TossPaymentConfirmRequest request) {
        PaymentConfirmCommand command = PaymentConfirmCommand.builder()
                .paymentKey(request.getPaymentKey())
                .orderId(request.getOrderId())
                .amount(Long.valueOf(request.getAmount()))
                .build();

        PaymentConfirmationResult result = confirmFacade.confirm(command);
        return ResponseEntity.ok()
                .body(CommonResponse.success(result));
    }

}
