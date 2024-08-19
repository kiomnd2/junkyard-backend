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

    @Transactional
    @Override
    public void reserve(String authId, ReservationCommand reservationCommand) {
        MemberUser memberUser = memberReader.checkMember(Long.parseLong(authId));

        Reservation reservation = reservationCommand.toEntity(memberUser);
        reservationStore.storeReservation(reservation);
    }
}
