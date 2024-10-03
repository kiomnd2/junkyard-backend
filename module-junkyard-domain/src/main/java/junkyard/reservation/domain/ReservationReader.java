package junkyard.reservation.domain;

import junkyard.member.domain.MemberUser;

import java.util.List;

public interface ReservationReader {
    Reservation read(MemberUser memberUser, String reservationId);

    Reservation read(String idempotencyKey);
    List<Reservation> readByUser(MemberUser memberUser);

}