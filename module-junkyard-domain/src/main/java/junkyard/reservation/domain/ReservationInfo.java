package junkyard.reservation.domain;

import junkyard.car.domain.CarInfo;
import junkyard.common.response.exception.payment.CheckoutStatusException;
import junkyard.common.response.exception.reservation.InvalidEstimateException;
import junkyard.reservation.domain.estimate.EstimateInfo;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
import java.util.Set;

@Builder
public record ReservationInfo(String clientName, String phoneNo, String reservationId, String userId, CarInfo carInfo, LocalDateTime startTime
        , LocalDateTime endTime, Reservation.State status, String contents, String cancellationReason, List<EstimateInfo> estimateInfos,
                              LocalDateTime createdAt, LocalDateTime updatedAt
) {

    public void checkPayment() {
        if (status != Reservation.State.COMPLETED) {
            throw new CheckoutStatusException(status.name());
        }
    }

    public BigDecimal getTopEstimate() {
        EstimateInfo estimateInfo = estimateInfos.stream().filter(EstimateInfo::isFinal).findFirst()
                .orElseThrow(InvalidEstimateException::new);
        return BigDecimal.valueOf(estimateInfo.amount());
    }
}
