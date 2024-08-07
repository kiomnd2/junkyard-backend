package junkyard.reservation.infrastructure;

import junkyard.reservation.domain.Reservation;
import junkyard.reservation.domain.ReservationStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReservationStoreImpl implements ReservationStore {
    @Override
    public Reservation storeReservation(Reservation reservation) {
        return null;
    }
}
