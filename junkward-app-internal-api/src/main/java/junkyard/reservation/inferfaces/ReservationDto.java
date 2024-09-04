package junkyard.reservation.inferfaces;

import junkyard.car.domain.CarInfo;
import junkyard.reservation.domain.ReservationCommand;
import junkyard.reservation.domain.ReservationInfo;
import junkyard.reservation.domain.estimate.EstimateInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        private String clientName;
        private String contents;
        private RequestCars car;

        public ReservationCommand toCommand() {
            return ReservationCommand.builder()
                    .idempotencyKey(idempotencyKey)
                    .content(contents)
                    .clientName(clientName)
                    .car(car.toCommand())
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class RequestCancelReservation {
        private String idempotencyKey;
        private String cancelReason;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class RequestConfirmReservation {
        private String idempotencyKey;
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class ResponseInquireReservation {
        private String reservationId;
        private String userId;
        private String clientName;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String status;
        private ResponseCar car;
        private List<ResponseEstimate> estimate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static ResponseInquireReservation by(ReservationInfo info) {
            return ResponseInquireReservation.builder()
                    .reservationId(info.reservationId())
                    .userId(info.userId())
                    .clientName(info.clientName())
                    .startTime(info.startTime())
                    .endTime(info.endTime())
                    .status(info.status())
                    .car(ResponseCar.by(info.carInfo()))
                    .estimate(info.estimateInfos().stream()
                            .map(ResponseEstimate::by)
                            .collect(Collectors.toList()))
                    .createdAt(info.createdAt())
                    .updatedAt(info.updatedAt())
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class ResponseCar {
        private String make;
        private String model;
        private String licensePlate;

        public static ResponseCar by(CarInfo carInfo) {
            return ResponseCar.builder()
                    .make(carInfo.make())
                    .licensePlate(carInfo.licensePlate())
                    .model(carInfo.model())
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class RequestEstimate {
        private String idempotencyKey;
        private String description;
        private Double amount;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class ResponseEstimate {
        private Double amount;
        private LocalDateTime issuedDate;
        private String description;
        private Boolean isFinal;

        public static ResponseEstimate by(EstimateInfo estimateInfo) {
            return ResponseEstimate.builder()
                    .amount(estimateInfo.amount())
                    .isFinal(estimateInfo.isFinal())
                    .issuedDate(estimateInfo.issuedDate())
                    .description(estimateInfo.description())
                    .build();
        }
    }


}
