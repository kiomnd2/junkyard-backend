package junkyard.reservation.application;

import junkyard.reservation.domain.ReservationInfo;
import junkyard.reservation.domain.ReservationService;
import junkyard.reservation.inferfaces.ReservationDto;
import junkyard.telegram.client.TelegramMessageSender;
import junkyard.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationFacade {
    private final ReservationService reservationService;
    private final TelegramMessageSender messageSender;

    public void reserve(Long authId, ReservationDto.RequestReservation requestReservation) {
        reservationService.reserve(authId, requestReservation.toCommand());
        messageSender.sendMessage("[%s][%s] 님이 예약했습니다.", DateTimeUtils.toString(LocalDateTime.now())
                , requestReservation.getClientName());
    }

    public void updateReservation(String username, String idempotencyKey, String contents) {
        reservationService.updateReservation(username, idempotencyKey, contents);
        messageSender.sendMessage("[%s][%s][%s] 님이 내용을 업데이트 했습니다. 사유 : %s",
                idempotencyKey, DateTimeUtils.toString(LocalDateTime.now())
                , username, contents);
    }

    public void cancelReservation(String username, String idempotencyKey, String cancelReason) {
        reservationService.cancelReservation(username, idempotencyKey, cancelReason);
        messageSender.sendMessage("[%s][%s] 님이 예약을 취소했습니다. 사유 : %s",
                DateTimeUtils.toString(LocalDateTime.now())
                , username, cancelReason);
    }

    public void confirm(String idempotencyKey) {
        reservationService.confirm(idempotencyKey);
    }

    public List<ReservationInfo> inquireList(Long authId) {
        return reservationService.inquireList(authId);
    }

    public void estimate(String idempotencyKey, Double amount, String description) {
        reservationService.addEstimate(idempotencyKey, amount, description);
    }

}
