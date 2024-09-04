package junkyard.reservation.application;

import junkyard.reservation.domain.ReservationInfo;
import junkyard.reservation.domain.ReservationService;
import junkyard.reservation.inferfaces.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationFacade {
    private final ReservationService reservationService;

    public void reserve(Long authId, ReservationDto.RequestReservation requestReservation) {
        reservationService.reserve(authId, requestReservation.toCommand());
    }

    public void cancelReservation(String username, String idempotencyKey, String cancelReason) {
        reservationService.cancelReservation(username, idempotencyKey, cancelReason);
    }

    public void confirm(String idempotencyKey) {
        reservationService.confirm(idempotencyKey);
    }

    public List<ReservationInfo> inquireList(Long authId) {
        return reservationService.inquireList(authId);
    }

    public void estimate(String idempotencyKey, Double amount, String description) {
        reservationService.addEstimate(idempotencyKey, amount, description);
    }
}
