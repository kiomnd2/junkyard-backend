package junkyard.payment.application;

import junkyard.common.response.exception.PaymentValidatorException;
import junkyard.payment.domain.PaymentCommand;
import junkyard.payment.domain.PaymentExecutionResult;
import junkyard.payment.domain.PaymentExecutor;
import junkyard.payment.domain.confirm.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfirmFacade {
    private final PaymentConfirmService paymentConfirmService;
    private final PaymentValidator validator;
    private final PaymentExecutor paymentExecutor;


    public PaymentConfirmationResult confirm(PaymentConfirmCommand command) {
        if (validator.isValid(command.getOrderId(), command.getAmount())) {
            paymentConfirmService.changeOrderStatusToExecuting(command);
            paymentConfirmService.changePaymentKey(command.getOrderId(), command.getPaymentKey());
            PaymentExecutionResult result = paymentExecutor.execute(PaymentCommand.ConfirmCommand.builder()
                    .paymentKey(command.getPaymentKey())
                    .amount(command.getAmount())
                    .orderId(command.getOrderId())
                    .build()).block();
            PaymentStatusUpdateCommand updateCommand = PaymentStatusUpdateCommand.by(result);
            paymentConfirmService.updateOrderStatus(updateCommand);

            return PaymentConfirmationResult.builder()
                    .status(updateCommand.getStatus())
                    .failure(updateCommand.getFailure())
                    .build();
        }

        throw new PaymentValidatorException(command.getOrderId(), command.getAmount());
    }
}
