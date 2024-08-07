package junkyard.reservation.inferfaces;

import junkyard.member.domain.MemberUser;
import junkyard.reservation.application.ReservationFacade;
import junkyard.response.CommonResponse;
import junkyard.utils.IdempotencyKeyCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/reservation")
public class ReservationApi {
    private final ReservationFacade reservationFacade;

    @PostMapping("/checkout")
    public CommonResponse<ReservationDto.ResponseReservationCheckout> checkoutReservation(@AuthenticationPrincipal MemberUser memberUser) {
        return CommonResponse.success(
                ReservationDto.ResponseReservationCheckout.builder()
                        .idempotencyKey(IdempotencyKeyCreator.create(memberUser.getAuthId()))
                        .build()
        );
    }

    public CommonResponse<ReservationDto.ResponseReservation> reservation(@AuthenticationPrincipal MemberUser memberUser,
                                                                          @RequestBody ReservationDto.RequestReservation requestReservation) {

        return CommonResponse.success(ReservationDto.ResponseReservation.builder().build());
    }

}
