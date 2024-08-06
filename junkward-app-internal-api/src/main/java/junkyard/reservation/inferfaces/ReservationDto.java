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
    public static class RequestReservation {
        private String reservationId;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class ResponseReservation {

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class RequestCheckout {
        private String seed;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class ResponseCheckout {
        private String idempotencyKey;
    }
}
