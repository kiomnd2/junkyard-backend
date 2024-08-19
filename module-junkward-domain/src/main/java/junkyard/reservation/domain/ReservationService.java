package junkyard.reservation.domain;

public interface ReservationService {
    void reserve(String authId, ReservationCommand reservationCommand);
    void cancelReservation(String username, String idempotencyKey, String cancelReason);
    void confirm(String username, String idempotencyKey);
    ReservationInquireList inquireList(String username);
}
