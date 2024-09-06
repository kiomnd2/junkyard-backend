package junkyard.reservation.domain;

import junkyard.car.domain.CarInfo;
import junkyard.reservation.domain.estimate.EstimateInfo;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
public record ReservationInfo(String clientName, String phoneNo, String reservationId, String userId, CarInfo carInfo, LocalDateTime startTime
        , LocalDateTime endTime, String status, String content, String cancellationReason, List<EstimateInfo> estimateInfos,
                              LocalDateTime createdAt, LocalDateTime updatedAt
) {

}
