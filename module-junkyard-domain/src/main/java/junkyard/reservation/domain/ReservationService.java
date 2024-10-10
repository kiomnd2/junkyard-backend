package junkyard.reservation.domain;

import java.util.List;

public interface ReservationService {
    void reserve(Long authId, ReservationCommand reservationCommand);
    void updateReservation(String username, String idempotencyKey, String contents);
    void cancelReservation(String username, String idempotencyKey, String cancelReason);
    void confirm(String idempotencyKey);
    ReservationInfo inquireInfo(Long authId, String idempotencyKey);
    List<ReservationInfo> inquireList(Long authId);
    void addEstimate(String idempotencyKey, Double amount, String description);


}
