package junkyard.reservation.application;

import junkyard.reservation.inferfaces.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReservationFacade {
    public Long reserve(ReservationDto.RequestReservation requestReservation) {
        return 0L;
    }
}
