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
    public ReservationResult reserve(String authId, ReservationCommand reservationCommand) {
        Optional<MemberUser> memberUser = memberReader.readByAuthId(Long.parseLong(authId));
        MemberUser member = memberUser.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Reservation reservation = reservationCommand.toEntity(member);


        return null;
    }
}
