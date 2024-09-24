package junkyard.reservation.domain;

import java.util.List;

public interface ReservationService {
    void reserve(Long authId, ReservationCommand reservationCommand);
    void cancelReservation(String username, String idempotencyKey, String cancelReason);
    void confirm(String idempotencyKey);
    List<ReservationInfo> inquireList(Long authId);
    void addEstimate(String idempotencyKey, Double amount, String description);
}
