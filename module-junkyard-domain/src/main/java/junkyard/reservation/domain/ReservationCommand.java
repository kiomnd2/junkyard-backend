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
    private String contents;
    private CarCommand car;

    public Reservation toEntity(MemberUser member) {
        return Reservation.builder()
                .reservationId(idempotencyKey)
                .memberUser(member)
                .clientName(clientName)
                .car(car.toEntity())
                .phoneNo(member.getPhoneNo())
                .startTime(LocalDateTime.now())
                .contents(contents)
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

