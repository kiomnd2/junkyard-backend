package junkyard.payment.domain;

public interface PaymentExecutor {
    PaymentExecutionResult execute(PaymentCommand.ConfirmCommand command);
}
