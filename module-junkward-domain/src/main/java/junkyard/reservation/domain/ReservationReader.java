package junkyard.reservation.domain;

import junkyard.member.domain.MemberUser;

public interface ReservationReader {
    Reservation read(MemberUser memberUser, String reservationId);
}
