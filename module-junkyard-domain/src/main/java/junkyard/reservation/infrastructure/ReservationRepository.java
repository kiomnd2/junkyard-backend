package junkyard.reservation.infrastructure;

import junkyard.member.domain.MemberUser;
import junkyard.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByReservationIdAndMemberUser(String reservationId, MemberUser memberUser);
    List<Reservation> findAllByMemberUserOrderByUpdatedAtDesc(MemberUser memberUser);
    Reservation findByReservationId(String idempotencyKey);
}
