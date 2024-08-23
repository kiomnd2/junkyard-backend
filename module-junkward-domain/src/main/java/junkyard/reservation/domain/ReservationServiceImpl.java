package junkyard.reservation.domain;

import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.member.InvalidUserException;
import junkyard.member.domain.MemberReader;
import junkyard.member.domain.MemberUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {
    private final MemberReader memberReader;
    private final ReservationStore reservationStore;
    private final ReservationReader reservationReader;

    @Transactional
    @Override
    public void reserve(Long authId, ReservationCommand reservationCommand) {
        MemberUser memberUser = memberReader.checkMember(authId);
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
    public void confirm(Long username, String idempotencyKey) {
        MemberUser memberUser = memberReader.checkMember(username);
        Reservation reservation = reservationReader.read(memberUser, idempotencyKey);
        reservation.confirm();
    }

    @Override
    public List<ReservationInfo> inquireList(Long authId) {
        MemberUser memberUser = memberReader.readByAuthId(authId)
                .orElseThrow(() -> new InvalidUserException(Codes.COMMON_INVALID_MEMBER, authId));
        return reservationReader.readByUser(memberUser).stream().map(Reservation::toInfo).toList();
    }
}
