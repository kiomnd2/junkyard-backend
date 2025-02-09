package junkyard.payment.domain.checkout;

import junkyard.car.domain.CarInfo;
import junkyard.payment.domain.PaymentEvent;
import junkyard.payment.domain.PaymentStore;
import junkyard.payment.domain.order.PaymentOrder;
import junkyard.reservation.domain.Reservation;
import junkyard.reservation.domain.ReservationInfo;
import junkyard.reservation.domain.ReservationService;
import junkyard.reservation.domain.estimate.EstimateInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private ReservationService reservationService;

    @Mock
    private PaymentStore paymentStore;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    static ReservationInfo mockReservationInfo(String clientName, Double amount, String reservationId) {
        CarInfo carInfo = new CarInfo("Honda", "Civic", "XYZ987");
        EstimateInfo estimateInfo = new EstimateInfo(amount, LocalDateTime.now(), "Initial estimate", true);
        List<EstimateInfo> estimateInfos = List.of(estimateInfo);

        return new ReservationInfo(
                clientName,
                "987-654-3210",
                reservationId,
                "user789",
                carInfo,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                Reservation.State.COMPLETED,
                "No specific contents",
                null,
                estimateInfos,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void testCheckoutSuccess() {
        Long authId = 1L;
        CheckoutCommand command = CheckoutCommand.builder()
                .reservationId("reservationId")
                .idempotencyKey("test-idempotency-key")
                .buyerId(1L)
                .build();

        ReservationInfo reservationInfo = mock(ReservationInfo.class);
        when(reservationService.inquireInfo(authId, command.getReservationId()))
                .thenReturn(mockReservationInfo("client", 100.0, "reservationId"));

        PaymentEvent storedEvent = PaymentEvent.builder()
                .buyerId(authId)
                .orderId("test-idempotency-key")
                .orderName("client")
                .build();

        storedEvent.addPaymentOrder(PaymentOrder.builder()
                .sellerId(1L)
                .orderId(command.getIdempotencyKey())
                .productId(command.getReservationId())
                .amount(reservationInfo.getTopEstimate())
                .paymentOrderStatus(PaymentOrder.PaymentOrderStatus.NOT_STARTED)
                .build());

        when(paymentStore.store(any(PaymentEvent.class))).thenReturn(storedEvent);

        // When
        CheckoutResult result = checkoutService.checkout(authId, command);

        assertNotNull(result);
        assertEquals("test-idempotency-key", result.getOrderId());
        assertEquals("client", result.getOrderName());
        assertEquals(BigDecimal.valueOf(100), result.getAmount());

        verify(reservationService).inquireInfo(authId, command.getReservationId());
        verify(paymentStore).store(any(PaymentEvent.class));
    }

}