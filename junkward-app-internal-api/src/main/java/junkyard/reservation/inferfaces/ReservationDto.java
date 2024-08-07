package junkyard.reservation.inferfaces;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class RequestReservation {
        private String contents;
        private List<RequestCars> carList;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class RequestCars {
        private String make;
        private String model;
        private String licensePlate;
    }

    @NoArgsConstructor
    @Builder
    @Getter
    public static class ResponseReservation {

    }
}
