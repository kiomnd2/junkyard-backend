package junkyard.reservation.domain;

public interface ReservationService {
    ReservationResult reserve(String authId, ReservationCommand reservationCommand);
}
