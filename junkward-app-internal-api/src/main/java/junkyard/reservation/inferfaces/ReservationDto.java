package junkyard.reservation.inferfaces;

import junkyard.reservation.domain.ReservationCommand;
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
        private String idempotencyKey;
        private String contents;
        private RequestCars car;

        public ReservationCommand toCommand() {
            return ReservationCommand.builder()
                    .idempotencyKey(idempotencyKey)
                    .content(contents)
                    .car(car.toCommand())
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class RequestCars {
        private String make;
        private String model;
        private String licensePlate;

        public ReservationCommand.CarCommand toCommand() {
            return ReservationCommand.CarCommand.builder()
                    .make(make)
                    .model(model)
                    .licensePlate(licensePlate)
                    .build();
        }
    }

    @NoArgsConstructor
    @Builder
    @Getter
    public static class ResponseReservation {

    }
}
