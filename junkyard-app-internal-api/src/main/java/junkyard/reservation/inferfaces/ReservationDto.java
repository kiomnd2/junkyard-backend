package junkyard.reservation.inferfaces;

import jakarta.validation.constraints.*;
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
        @NotNull(message = "idempotencyKey는 필수 값입니다.")
        private String idempotencyKey;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class RequestReservation {

        @NotNull(message = "idempotencyKey 는 필수 값입니다.")
        private String idempotencyKey;

        @NotBlank(message = "clientName 필수 값입니다.")
        @Size(max = 100, message = "Name은 최대 100자까지 허용됩니다.")
        private String clientName;

        @NotBlank(message = "Phone number 는 필수 값입니다.")
        @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "유효한 전화번호를 입력해주세요.")
        private String phoneNo;
        private String contents;
        private RequestCars car;

        public ReservationCommand toCommand() {
            return ReservationCommand.builder()
                    .idempotencyKey(idempotencyKey)
                    .contents(contents)
                    .phoneNo(phoneNo)
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

        @NotNull(message = "idempotencyKey 는 필수 값입니다.")
        private String idempotencyKey;

        @NotNull(message = "cancelReason 는 필수 값입니다.")
        private String cancelReason;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class RequestConfirmReservation {

        @NotNull(message = "idempotencyKey 는 필수 값입니다.")
        private String idempotencyKey;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class RequestCars {

        @NotNull(message = "제조사는 필수 값입니다.")
        private String make;

        @NotNull(message = "모델명은 필수 값입니다.")
        private String model;

        @NotNull(message = "번호키 는 필수 값입니다.")
        private String licensePlate;

        public ReservationCommand.CarCommand toCommand() {
            return ReservationCommand.CarCommand.builder()
                    .make(make)
                    .model(model)
                    .licensePlate(licensePlate)
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class ResponseInquireReservation {
        private String reservationId;
        private String userId;
        private String clientName;
        private String phoneNo;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String status;
        private String contents;
        private ResponseCar car;
        private List<ResponseEstimate> estimate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static ResponseInquireReservation by(ReservationInfo info) {
            return ResponseInquireReservation.builder()
                    .reservationId(info.reservationId())
                    .userId(info.userId())
                    .phoneNo(info.phoneNo())
                    .clientName(info.clientName())
                    .startTime(info.startTime())
                    .endTime(info.endTime())
                    .status(info.status())
                    .contents(info.contents())
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

        @NotNull(message = "idempotencyKey 는 필수 값입니다.")
        private String idempotencyKey;

        @Min(value = 0, message = "0 이하의 금액을 입력받을 수 없습니다")
        @NotNull(message = "amount 는 필수 값입니다.")
        private Double amount;
        private String description;
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class RequestUpdateReservation {
        @NotNull(message = "idempotencyKey 는 필수 값입니다.")
        private String idempotencyKey;

        @NotNull(message = "contents 는 필수 값입니다.")
        private String contents;
    }
}
