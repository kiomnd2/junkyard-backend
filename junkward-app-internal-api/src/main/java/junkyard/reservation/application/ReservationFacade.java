package junkyard.reservation.application;

import junkyard.reservation.domain.ReservationService;
import junkyard.reservation.inferfaces.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReservationFacade {
    private final ReservationService reservationService;

    public void reserve(String userName, ReservationDto.RequestReservation requestReservation) {
        reservationService.reserve(userName, requestReservation.toCommand());
    }

    public void cancelReservation(String username, String idempotencyKey, String cancelReason) {
        reservationService.cancelReservation(username, idempotencyKey, cancelReason);
    }
}
