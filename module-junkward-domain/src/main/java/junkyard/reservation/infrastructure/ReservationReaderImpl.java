package junkyard.reservation.infrastructure;

import junkyard.common.response.codes.Codes;
import junkyard.member.domain.MemberUser;
import junkyard.reservation.domain.Reservation;
import junkyard.reservation.domain.ReservationReader;
import junkyard.common.response.exception.reservation.InvalidReservationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ReservationReaderImpl implements ReservationReader {
    private final ReservationRepository reservationRepository;

    @Override
    public Reservation read(MemberUser memberUser, String reservationId) {
        return reservationRepository.findByReservationIdAndMemberUser(reservationId, memberUser)
                .orElseThrow(() -> new InvalidReservationException(Codes.INVALID_RESERVATION, reservationId, memberUser.getName())) ;
    }

    @Override
    public List<Reservation> readByUser(MemberUser memberUser) {
        return reservationRepository.findAllByMemberUser(memberUser);
    }

    @Override
    public Reservation read(String idempotencyKey) {
        return reservationRepository.findByReservationId(idempotencyKey);
    }
}
