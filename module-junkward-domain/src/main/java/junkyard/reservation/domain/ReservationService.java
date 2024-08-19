package junkyard.reservation.domain;

public interface ReservationService {
    void reserve(String authId, ReservationCommand reservationCommand);
}
