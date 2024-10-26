package junkyard.payment;

public interface PaymentExecutor {
    PaymentExecutionResult execute(PaymentCommand.ConfirmCommand command);
}
