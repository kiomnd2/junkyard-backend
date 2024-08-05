package junkyard.reservation.inferfaces;

import junkyard.reservation.application.ReservationFacade;
import junkyard.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/reservation")
public class ReservationApi {
    private final ReservationFacade reservationFacade;

    public CommonResponse<ReservationDto.ResponseReservation> reservation(@RequestBody ReservationDto.RequestReservation requestReservation) {
        return CommonResponse.success(ReservationDto.ResponseReservation.builder().build());
    }

}
