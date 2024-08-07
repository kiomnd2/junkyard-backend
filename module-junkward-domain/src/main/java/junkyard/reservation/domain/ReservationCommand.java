package junkyard.reservation.domain;

import junkyard.member.domain.MemberUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReservationCommand {
    private String content;
    private Car car;

    public Reservation toEntity(MemberUser member) {
        return Reservation.builder()
                .memberUser(member)
                .car(car.toEntity())
                .content(content)
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Car {
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

