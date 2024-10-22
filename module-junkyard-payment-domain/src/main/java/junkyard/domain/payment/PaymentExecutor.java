package junkyard.domain.payment;

public interface PaymentExecutor {
    PaymentExecutionResult execute(PaymentCommand.ConfirmCommand command);
}
