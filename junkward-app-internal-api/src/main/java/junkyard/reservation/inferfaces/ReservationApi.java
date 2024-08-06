package junkyard.reservation.inferfaces;

import junkyard.reservation.application.ReservationFacade;
import junkyard.response.CommonResponse;
import junkyard.utils.IdempotencyKeyCreator;
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

    @PostMapping("/checkout")
    public CommonResponse<ReservationDto.ResponseCheckout> checkoutReservation(@RequestBody ReservationDto.RequestCheckout checkout) {
        return CommonResponse.success(
                ReservationDto.ResponseCheckout.builder()
                        .idempotencyKey(IdempotencyKeyCreator.create(checkout.getSeed()))
                        .build()
        );
    }

    public CommonResponse<ReservationDto.ResponseReservation> reservation(@RequestBody ReservationDto.RequestReservation requestReservation) {
        return CommonResponse.success(ReservationDto.ResponseReservation.builder().build());
    }

}
