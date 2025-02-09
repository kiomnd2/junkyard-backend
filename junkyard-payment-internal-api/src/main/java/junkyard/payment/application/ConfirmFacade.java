package junkyard.payment.application;

import io.netty.handler.timeout.TimeoutException;
import junkyard.common.response.exception.PaymentValidatorException;
import junkyard.common.response.exception.payment.PaymentAlreadyProcessedException;
import junkyard.payment.domain.PaymentCommand;
import junkyard.payment.domain.PaymentEvent;
import junkyard.payment.domain.PaymentExecutionResult;
import junkyard.payment.domain.PaymentExecutor;
import junkyard.payment.domain.confirm.*;
import junkyard.payment.domain.exception.PaymentValidationException;
import junkyard.payment.domain.order.PaymentOrder;
import junkyard.payment.infrastructure.payment.toss.exception.PSPConfirmationException;
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
            try {
                paymentConfirmService.changeOrderStatusToExecuting(command);
                paymentConfirmService.changePaymentKey(command.getOrderId(), command.getPaymentKey());
                PaymentExecutionResult result = paymentExecutor.execute(PaymentCommand.ConfirmCommand.builder()
                        .paymentKey(command.getPaymentKey())
                        .amount(command.getAmount())
                        .orderId(command.getOrderId())
                        .build()).block();
                PaymentStatusUpdateCommand updateCommand = PaymentStatusUpdateCommand.by(result);
                PaymentEvent event = paymentConfirmService.updateOrderStatus(updateCommand);

                return PaymentConfirmationResult.builder()
                        .status(updateCommand.getStatus())
                        .failure(updateCommand.getFailure())
                        .build();
            } catch (PSPConfirmationException e) {
                return processPSPConfirmation(command, e);
            } catch (PaymentValidationException ve) {
                return processPaymentValidation(command, ve);
            } catch (PaymentAlreadyProcessedException ape) {
                return processPaymentAlreadyProcess(ape);
            } catch (Exception e) {
                return processError(command, e);
            }
        }
        throw new PaymentValidatorException(command.getOrderId(), command.getAmount());
    }

    private PaymentConfirmationResult processError(PaymentConfirmCommand command, Exception e) {
        PaymentExecutionResult.PaymentExecutionFailure f =
                PaymentExecutionResult.PaymentExecutionFailure.builder()
                        .errorCode(e.getClass().getSimpleName())
                        .message(e.getMessage()).build();

        PaymentStatusUpdateCommand statusUpdateCommand = PaymentStatusUpdateCommand.builder()
                .paymentKey(command.getPaymentKey())
                .orderId(command.getOrderId())
                .status(PaymentOrder.PaymentOrderStatus.UNKNOWN)
                .failure(f)
                .build();

        paymentConfirmService.updateOrderStatus(statusUpdateCommand);
        return PaymentConfirmationResult.builder()
                .status(PaymentOrder.PaymentOrderStatus.UNKNOWN)
                .failure(f)
                .build();
    }

    private static PaymentConfirmationResult processPaymentAlreadyProcess(PaymentAlreadyProcessedException ape) {
        return PaymentConfirmationResult.builder()
                .status(PaymentOrder.PaymentOrderStatus.FAILURE)
                .failure(PaymentExecutionResult.PaymentExecutionFailure.builder()
                        .errorCode(ape.getClass().getSimpleName())
                        .message(ape.getMessage()).build())
                .build();
    }

    private static PaymentConfirmationResult processPaymentValidation(PaymentConfirmCommand command, PaymentValidationException ve) {
        PaymentExecutionResult.PaymentExecutionFailure f = PaymentExecutionResult.PaymentExecutionFailure.builder()
                .errorCode(ve.getClass().getSimpleName())
                .message(ve.getMessage()).build();
        PaymentStatusUpdateCommand.builder()
                .paymentKey(command.getPaymentKey())
                .orderId(command.getOrderId())
                .status(PaymentOrder.PaymentOrderStatus.FAILURE)
                .failure(f)
                .build();

        return PaymentConfirmationResult.builder()
                .status(PaymentOrder.PaymentOrderStatus.FAILURE)
                .failure(f)
                .build();
    }

    private PaymentConfirmationResult processPSPConfirmation(PaymentConfirmCommand command, PSPConfirmationException e) {
        PaymentOrder.PaymentOrderStatus paymentOrderStatus = e.paymentStatus();
        PaymentExecutionResult.PaymentExecutionFailure t =
                PaymentExecutionResult.PaymentExecutionFailure.builder()
                        .errorCode(e.getErrorCode())
                        .message(e.getErrorMessage()).build();

        PaymentStatusUpdateCommand statusUpdateCommand = PaymentStatusUpdateCommand.builder()
                .paymentKey(command.getPaymentKey())
                .orderId(command.getOrderId())
                .status(paymentOrderStatus)
                .failure(t)
                .build();

        paymentConfirmService.updateOrderStatus(statusUpdateCommand);
        return PaymentConfirmationResult.builder()
                .status(paymentOrderStatus)
                .failure(t)
                .build();
    }
}
