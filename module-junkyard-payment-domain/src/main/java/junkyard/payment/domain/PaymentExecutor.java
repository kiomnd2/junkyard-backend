package junkyard.payment.domain;

import reactor.core.publisher.Mono;

public interface PaymentExecutor {
    Mono<PaymentExecutionResult> execute(PaymentCommand.ConfirmCommand command);
}
