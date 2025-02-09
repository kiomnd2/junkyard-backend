package junkyard.payment.application;

import junkyard.payment.domain.PaymentCommand;
import junkyard.payment.domain.PaymentEvent;
import junkyard.payment.domain.PaymentExecutionResult;
import junkyard.payment.domain.PaymentExecutor;
import junkyard.payment.domain.confirm.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConfirmFacadeTest {

    @Mock
    private PaymentConfirmService paymentConfirmService;

    @Mock
    private PaymentValidator paymentValidator;

    @Mock
    private PaymentExecutor paymentExecutor;

    @InjectMocks
    private ConfirmFacade confirmFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConfirmSuccess() {
        PaymentConfirmCommand command = PaymentConfirmCommand.builder()
                .orderId("order123")
                .paymentKey("paymentKey123")
                .amount(1000L)
                .build();

        when(paymentValidator.isValid(command.getOrderId(), command.getAmount())).thenReturn(true);

        PaymentExecutionResult successfulResult = PaymentExecutionResult.builder()
                .paymentKey(command.getPaymentKey())
                .orderId(command.getOrderId())
                .isSuccess(true)
                .isFailure(false)
                .isUnknown(false)
                .isRetryable(false)
                .paymentExtraDetails(PaymentExecutionResult.PaymentExtraDetails.builder()
                        .orderName("order123")
                        .totalAmount(1000L)
                        .build())
                .build();

        when(paymentExecutor.execute(any(PaymentCommand.ConfirmCommand.class)))
                .thenReturn(Mono.just(successfulResult));

        PaymentEvent paymentEvent = PaymentEvent.builder()
                .buyerId(1L)
                .paymentKey(command.getPaymentKey())
                .orderId(command.getOrderId())
                .orderName("Test Order")
                .method(PaymentEvent.PaymentMethod.EASY_PAY)
                .build();

        when(paymentConfirmService.updateOrderStatus(any(PaymentStatusUpdateCommand.class)))
                .thenReturn(paymentEvent);

        PaymentConfirmationResult result = confirmFacade.confirm(command);

    }
}
