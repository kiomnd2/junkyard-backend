package junkyard.reservation.inferfaces;

import junkyard.reservation.application.ReservationFacade;
import junkyard.response.CommonResponse;
import junkyard.security.annotataion.UserAuthorize;
import junkyard.utils.IdempotencyCreator;
import lombok.RequiredArgsConstructor;
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
    public CommonResponse<ReservationDto.ResponseReservationCheckout> issueIdempotencyKey() {
        return CommonResponse.success(ReservationDto.ResponseReservationCheckout.builder()
                .idempotencyKey(IdempotencyCreator.create("seed"))
                .build());
    }

    @UserAuthorize
    @PostMapping
    public CommonResponse<ReservationDto.ResponseReservation> reservation(@RequestBody ReservationDto.RequestReservation requestReservation) {
        reservationFacade.reserve(requestReservation);
        return CommonResponse.success(ReservationDto.ResponseReservation.builder().build());
    }

}
