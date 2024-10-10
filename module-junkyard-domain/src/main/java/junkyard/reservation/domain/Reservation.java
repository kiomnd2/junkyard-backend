package junkyard.reservation.domain;


import jakarta.persistence.*;
import junkyard.BaseEntity;
import junkyard.car.domain.Car;
import junkyard.common.response.codes.Codes;
import junkyard.common.response.exception.reservation.InvalidCancelRequestException;
import junkyard.member.domain.MemberUser;
import junkyard.reservation.domain.estimate.Estimate;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_id", nullable = false, unique = true)
    private String reservationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberUser memberUser;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private State status;

    @Column(name = "content", nullable = false, length = 500)
    private String contents;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Estimate> estimates = new ArrayList<>();

    @Builder
    public Reservation(String reservationId, MemberUser memberUser, String clientName, String phoneNo, LocalDateTime startTime, Car car, String contents) {
        this.reservationId = reservationId;
        this.memberUser = memberUser;
        this.car = car;
        this.startTime = startTime;
        this.endTime = LocalDateTime.now();
        this.status = State.PENDING;
        this.clientName = clientName;
        this.contents = contents;
        this.phoneNo = phoneNo;
    }

    public void cancel(String cancelReason) {
        if (status == State.PENDING) {
            this.status = State.CANCELED;
            this.cancellationReason = cancelReason;
        }
        throw new InvalidCancelRequestException(Codes.INVALID_RESERVATION_CANCEL,
                "현재상태는 취소할 수 없는 상태"
                , this.status.name());
    }

    public void confirm() {
        if (status == State.PENDING) {
            this.status = State.CONFIRMED;
        }
        throw new InvalidCancelRequestException(Codes.INVALID_RESERVATION_CANCEL,
                "현재상태는 확인할 수 없는 상태"
                , this.status.name());
    }

    public ReservationInfo toInfo() {
        return ReservationInfo.builder()
                .clientName(clientName)
                .phoneNo(phoneNo)
                .reservationId(reservationId)
                .userId(memberUser.getAuthId().toString())
                .startTime(startTime)
                .endTime(endTime)
                .cancellationReason(cancellationReason)
                .contents(contents)
                .status(status.name())
                .carInfo(car.toInfo())
                .estimateInfos(estimates.stream()
                        .map(Estimate::toInfo)
                        .collect(Collectors.toList())
                )
                .build();
    }

    public void addEstimate(Double amount, String description) {
        this.estimates.forEach(Estimate::notFinal);

        this.estimates.add(Estimate.builder()
                        .reservation(this)
                        .isFinal(true)
                        .amount(amount)
                        .description(description)
                        .issuedDate(LocalDateTime.now())
                .build());
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }

    public enum State {
        PENDING, CONFIRMED, CANCELED, REJECTED, COMPLETED, EXPIRED, ON_HOLD
    }
}
