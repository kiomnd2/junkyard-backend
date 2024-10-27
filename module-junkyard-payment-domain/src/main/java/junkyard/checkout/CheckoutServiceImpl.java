package junkyard.checkout;

import junkyard.payment.PaymentEvent;
import junkyard.payment.order.PaymentOrder;
import junkyard.reservation.domain.ReservationInfo;
import junkyard.reservation.domain.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutServiceImpl implements CheckoutService {
    private final ReservationService reservationService;

    @Transactional
    @Override
    public CheckoutResult checkout(Long authId, CheckoutCommand command) {
        ReservationInfo reservationInfo = reservationService.inquireInfo(authId, command.getIdempotencyKey());
        reservationInfo.checkPayment();



        return null;
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
