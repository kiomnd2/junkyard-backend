package junkyard.reservation.domain;

import java.util.List;

public interface ReservationService {
    void reserve(String authId, ReservationCommand reservationCommand);
    void cancelReservation(String username, String idempotencyKey, String cancelReason);
    void confirm(Long username, String idempotencyKey);
    List<ReservationInfo> inquireList(Long authId);
}
