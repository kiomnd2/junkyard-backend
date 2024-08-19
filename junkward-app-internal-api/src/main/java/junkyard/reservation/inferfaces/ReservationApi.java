package junkyard.reservation.inferfaces;

import junkyard.member.domain.MemberUser;
import junkyard.reservation.application.ReservationFacade;
import junkyard.response.CommonResponse;
import junkyard.security.annotataion.UserAuthorize;
import junkyard.security.userdetails.MyUserDetails;
import junkyard.utils.IdempotencyCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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


    @UserAuthorize
    @PostMapping("/checkout")
    public CommonResponse<ReservationDto.ResponseReservationCheckout> issueIdempotencyKey(@AuthenticationPrincipal MyUserDetails userDetails) {
        return CommonResponse.success(ReservationDto.ResponseReservationCheckout.builder()
                .idempotencyKey(IdempotencyCreator.create(userDetails.getUsername()))
                .build());
    }

    @UserAuthorize
    @PostMapping
    public CommonResponse<Void> reservation(@AuthenticationPrincipal MyUserDetails userDetails,
                                                                          @RequestBody ReservationDto.RequestReservation requestReservation) {
        reservationFacade.reserve(userDetails.getUsername(), requestReservation);
        return CommonResponse.success(null);
    }

    @UserAuthorize
    @PostMapping("/cancel")
    public CommonResponse<Void> cancelReservation(@AuthenticationPrincipal MyUserDetails userDetails,
                                  @RequestBody ReservationDto.RequestCancelReservation cancelReservation) {
        reservationFacade.cancelReservation(userDetails.getUsername(),
                cancelReservation.getIdempotencyKey(),
                cancelReservation.getCancelReason());
        return CommonResponse.success(null);
    }
}
