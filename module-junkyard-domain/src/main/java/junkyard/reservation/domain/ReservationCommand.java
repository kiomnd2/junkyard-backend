package junkyard.reservation.domain;

import junkyard.member.domain.MemberUser;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Builder
public class ReservationCommand {
    private String clientName;
    private String phoneNo;
    private String idempotencyKey;
    private String content;
    private CarCommand car;

    public Reservation toEntity(MemberUser member) {
        return Reservation.builder()
                .reservationId(idempotencyKey)
                .memberUser(member)
                .clientName(clientName)
                .car(car.toEntity())
                .startTime(LocalDateTime.now())
                .content(content)
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CarCommand {
        private String make;
        private String model;
        private String licensePlate;

        public junkyard.car.domain.Car toEntity() {
            return junkyard.car.domain.Car.builder()
                    .make(make)
                    .model(model)
                    .licensePlate(licensePlate)
                    .build();
        }
    }
}

