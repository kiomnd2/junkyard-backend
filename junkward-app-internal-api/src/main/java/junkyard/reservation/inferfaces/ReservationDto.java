package junkyard.reservation.inferfaces;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReservationDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class RequestReservationCheckout {
        private String seed;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class ResponseReservationCheckout {
        private String idempotencyKey;
    }

    @NoArgsConstructor
    @Builder
    @Getter
    public static class RequestReservation {

    }

    @NoArgsConstructor
    @Builder
    @Getter
    public static class ResponseReservation {

    }
}
