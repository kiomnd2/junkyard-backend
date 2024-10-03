package junkyard.reservation.inferfaces;

import junkyard.reservation.application.ReservationFacade;
import junkyard.reservation.domain.ReservationInfo;
import junkyard.response.CommonResponse;
import junkyard.security.annotataion.AdminAuthorize;
import junkyard.security.annotataion.UserAuthorize;
import junkyard.security.userdetails.MyUserDetails;
import junkyard.utils.IdempotencyCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/reservation")
public class ReservationApi {
    private final ReservationFacade reservationFacade;

    @UserAuthorize
    @PostMapping("/checkout")
    public CommonResponse<ReservationDto.ResponseReservationCheckout> issueIdempotencyKey(@AuthenticationPrincipal MyUserDetails userDetails) {
        return CommonResponse.success(ReservationDto.ResponseReservationCheckout.builder()
                .idempotencyKey(IdempotencyCreator.create())
                .build());
    }

    @UserAuthorize
    @PostMapping
    public CommonResponse<Void> reservation(@AuthenticationPrincipal MyUserDetails userDetails,
                                                                          @RequestBody ReservationDto.RequestReservation requestReservation) {
        reservationFacade.reserve(Long.parseLong(userDetails.getUsername()), requestReservation);
        return CommonResponse.success(null);
    }

    @AdminAuthorize
    @UserAuthorize
    @PostMapping("/cancel")
    public CommonResponse<Void> cancelReservation(@AuthenticationPrincipal MyUserDetails userDetails,
                                  @RequestBody ReservationDto.RequestCancelReservation cancelReservation) {
        reservationFacade.cancelReservation(userDetails.getUsername(),
                cancelReservation.getIdempotencyKey(),
                cancelReservation.getCancelReason());
        return CommonResponse.success(null);
    }

    @UserAuthorize
    @GetMapping
    public CommonResponse<List<ReservationDto.ResponseInquireReservation>> inquireReservationList(@AuthenticationPrincipal MyUserDetails userDetails
    ) {
        List<ReservationInfo> reservationInfos = reservationFacade.inquireList(Long.parseLong(userDetails.getUsername()));
        return CommonResponse.success(reservationInfos.stream().map(ReservationDto.ResponseInquireReservation::by)
                .collect(Collectors.toList()));
    }

    @AdminAuthorize
    @PostMapping("/confirm")
    public CommonResponse<Void> confirmReservation(@AuthenticationPrincipal MyUserDetails userDetails,
                                                   @RequestBody ReservationDto.RequestConfirmReservation confirmReservation) {
        reservationFacade.confirm(confirmReservation.getIdempotencyKey());
        return CommonResponse.success(null);
    }

    @AdminAuthorize
    @PostMapping("/estimate")
    public CommonResponse<Void> estimateReservation(@RequestBody ReservationDto.RequestEstimate requestEstimate) {
        reservationFacade.estimate(requestEstimate.getIdempotencyKey(), requestEstimate.getAmount(), requestEstimate.getDescription());
        return CommonResponse.success(null);
    }
}
