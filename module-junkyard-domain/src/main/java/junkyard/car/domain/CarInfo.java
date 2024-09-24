package junkyard.car.domain;

import lombok.Builder;

@Builder
public record CarInfo(String make, String model, String licensePlate) {
}
