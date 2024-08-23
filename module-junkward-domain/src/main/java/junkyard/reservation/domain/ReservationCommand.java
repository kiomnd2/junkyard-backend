package junkyard.reservation.domain;

import junkyard.member.domain.MemberUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
public class ReservationCommand {
    private String idempotencyKey;
    private String content;
    private CarCommand car;

    public Reservation toEntity(MemberUser member) {
        return Reservation.builder()
                .reservationId(idempotencyKey)
                .memberUser(member)
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

