package junkyard.payment.domain.checkout;

import junkyard.payment.domain.PaymentEvent;
import junkyard.payment.domain.PaymentStore;
import junkyard.payment.domain.order.PaymentOrder;
import junkyard.reservation.domain.ReservationInfo;
import junkyard.reservation.domain.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CheckoutServiceImpl implements CheckoutService {
    private final ReservationService reservationService;
    private final PaymentStore paymentStore;


    @Transactional
    @Override
    public CheckoutResult checkout(Long authId, CheckoutCommand command) {
        ReservationInfo reservationInfo = reservationService.inquireInfo(authId, command.getReservationId());
        reservationInfo.checkPayment();
        PaymentEvent paymentEvent = createPaymentEvent(reservationInfo, authId, command);
        paymentStore.store(paymentEvent);

        return CheckoutResult.builder()
                .orderId(paymentEvent.getOrderId())
                .orderName(paymentEvent.getOrderName())
                .amount(BigDecimal.valueOf(paymentEvent.totalAmount()))
                .build();
    }

    @Transactional
    public PaymentEvent createPaymentEvent(ReservationInfo reservationInfo, Long authId, CheckoutCommand command) {
        PaymentEvent paymentEvent = PaymentEvent.builder()
                .buyerId(authId)
                .orderId(command.getIdempotencyKey())
                .orderName(reservationInfo.clientName())
                .build();

        paymentEvent.addPaymentOrder(PaymentOrder.builder()
                        .sellerId(1L)
                        .orderId(command.getIdempotencyKey())
                        .productId(reservationInfo.reservationId())
                        .amount(reservationInfo.getTopEstimate())
                        .paymentOrderStatus(PaymentOrder.PaymentOrderStatus.NOT_STARTED)
                .build());
        return paymentEvent;
    }
}
