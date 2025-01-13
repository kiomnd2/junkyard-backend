package junkyard.payment.infrastructure.payment.toss;

import junkyard.JunkyardPaymentApplication;
import junkyard.payment.domain.PaymentCommand;
import junkyard.payment.domain.PaymentExecutionResult;
import junkyard.payment.infrastructure.payment.toss.exception.PSPConfirmationException;
import junkyard.payment.infrastructure.payment.toss.exception.TossPaymentError;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
@Import(PSPTestWebClientConfiguration.class)
@SpringBootTest(classes = JunkyardPaymentApplication.class)
@Tag("tooLongTest")
@SpringBootConfiguration
class TossPaymentExecutorTest {

    @Autowired
    PSPTestWebClientConfiguration clientConfiguration;

    @Test
    void should_handle_correctly_various_TossPaymentError_scenarios() {
        List<ErrorScenario> errorScenarios = generateErrorScenarios();
        for (ErrorScenario errorScenario : errorScenarios) {
            PaymentCommand.ConfirmCommand command = PaymentCommand.ConfirmCommand.builder()
                    .paymentKey(UUID.randomUUID().toString())
                    .orderId(UUID.randomUUID().toString())
                    .amount(10000L)
                    .build();
            TossPaymentExecutor tossPaymentExecutor = new TossPaymentExecutor(clientConfiguration.createTestTossWebClient(
                    List.of(Pair.of("TossPayments-Test-Code", errorScenario.errorCode))
            ));
            tossPaymentExecutor.setUrl("/v1/payments/key-in");
            try{
                 tossPaymentExecutor.execute(command).block();
            } catch (PSPConfirmationException e) {
                System.out.println("e.paymentStatus() = " + e.paymentStatus());
                System.out.println("e.getIsSuccess() = " + e.getIsSuccess());
                System.out.println("e.getIsFailure() = " + e.getIsFailure());
                System.out.println("e.getIsUnknown() = " + e.getIsUnknown());
                System.out.println(e.getErrorCode());
                Assertions.assertThat(e.getIsSuccess()).isEqualTo(errorScenario.isSuccess);
                Assertions.assertThat(e.getIsFailure()).isEqualTo(errorScenario.isFailure);
                Assertions.assertThat(e.getIsUnknown()).isEqualTo(errorScenario.isUnknown);

            }
        }
    }

    private List<ErrorScenario> generateErrorScenarios() {
        return Arrays.stream(TossPaymentError.values())
                .map(tossPaymentError -> new ErrorScenario(tossPaymentError.name(),
                tossPaymentError.isSuccess(),
                tossPaymentError.isFailure(),
                tossPaymentError.isUnknown())).collect(Collectors.toList());
    }


    static class ErrorScenario {
        private String errorCode;
        private Boolean isSuccess;
        private Boolean isFailure;
        private Boolean isUnknown;

        public ErrorScenario(String errorCode, Boolean isSuccess, Boolean isFailure, Boolean isUnknown) {
            this.errorCode = errorCode;
            this.isSuccess= isSuccess;
            this.isFailure = isFailure;
            this.isUnknown = isUnknown;
        }
    }
}