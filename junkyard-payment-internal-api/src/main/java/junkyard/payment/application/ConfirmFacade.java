package junkyard.payment.application;

import junkyard.payment.domain.confirm.PaymentConfirmCommand;
import junkyard.payment.domain.confirm.PaymentConfirmService;
import junkyard.payment.domain.confirm.PaymentConfirmationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfirmFacade {
    private final PaymentConfirmService paymentConfirmService;

    public PaymentConfirmationResult confirm(PaymentConfirmCommand command) {
        return paymentConfirmService.confirm(command);
    }
}
