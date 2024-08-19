package junkyard.reservation.domain;

import junkyard.member.domain.MemberReader;
import junkyard.member.domain.MemberUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {
    private final MemberReader memberReader;
    private final ReservationStore reservationStore;
    private final ReservationReader reservationReader;

    @Transactional
    @Override
    public void reserve(String authId, ReservationCommand reservationCommand) {
        MemberUser memberUser = memberReader.checkMember(Long.parseLong(authId));

        Reservation reservation = reservationCommand.toEntity(memberUser);
        reservationStore.storeReservation(reservation);
    }

    @Transactional
    @Override
    public void cancelReservation(String username, String idempotencyKey, String cancelReason) {
        MemberUser memberUser = memberReader.checkMember(Long.parseLong(username));
        Reservation reservation = reservationReader.read(memberUser, idempotencyKey);
        reservation.cancel(cancelReason);
    }

    @Transactional
    @Override
    public void confirm(String username, String idempotencyKey) {
        MemberUser memberUser = memberReader.checkMember(Long.parseLong(username));
        Reservation reservation = reservationReader.read(memberUser, idempotencyKey);
        reservation.confirm();
    }


}
