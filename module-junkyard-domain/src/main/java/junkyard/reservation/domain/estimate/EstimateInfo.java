package junkyard.reservation.domain.estimate;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EstimateInfo(Double amount, LocalDateTime issuedDate, String description, Boolean isFinal) {
}
