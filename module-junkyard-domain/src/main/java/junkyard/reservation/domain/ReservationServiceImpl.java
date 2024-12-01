package junkyard.reservation.domain;

import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.member.InvalidUserException;
import junkyard.member.domain.MemberReader;
import junkyard.member.domain.MemberUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {
    private final MemberReader memberReader;
    private final ReservationStore reservationStore;
    private final ReservationReader reservationReader;

    @Transactional
    @Override
    public void reserve(Long authId, ReservationCommand reservationCommand) {
        log.info("reserveInfo (authId: {}, reservationCommand: {}", authId, reservationCommand);
        MemberUser memberUser = memberReader.checkMember(authId);
        Reservation reservation = reservationCommand.toEntity(memberUser);
        reservationStore.storeReservation(reservation);
    }

    @Transactional
    @Override
    public void updateReservation(String username, String idempotencyKey, String contents) {
        MemberUser memberUser = memberReader.checkMember(Long.parseLong(username));
        Reservation reservation = reservationReader.read(memberUser, idempotencyKey);
        reservation.updateContents(contents);
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
    public void confirm(String idempotencyKey) {
        Reservation reservation = reservationReader.read(idempotencyKey);
        reservation.confirm();
    }

    @Transactional
    @Override
    public ReservationInfo inquireInfo(Long authId, String reservationId) {
        return reservationReader.read(reservationId).toInfo();
    }

    @Transactional
    @Override
    public List<ReservationInfo> inquireList(Long authId) {
        MemberUser memberUser = memberReader.readByAuthId(authId)
                .orElseThrow(() -> new InvalidUserException(Codes.COMMON_INVALID_MEMBER, authId));
        return reservationReader.readByUser(memberUser).stream().map(Reservation::toInfo).toList();
    }

    @Transactional
    @Override
    public void addEstimate(String idempotencyKey, Double amount, String description) {
        Reservation reservation = reservationReader.read(idempotencyKey);
        reservation.addEstimate(amount, description);
    }

}
