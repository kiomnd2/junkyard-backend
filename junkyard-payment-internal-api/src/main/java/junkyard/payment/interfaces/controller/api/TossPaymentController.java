package junkyard.payment.interfaces.controller.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1/payment/toss")
public class TossPaymentController {
    @PostMapping("/confirm")
    public String confirm(@RequestBody TossPaymentDto.TossPaymentConfirmRequest request) {
        return "confirm";
    }

}
