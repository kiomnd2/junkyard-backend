package junkyard.reservation.domain;


import jakarta.persistence.*;
import junkyard.BaseEntity;
import junkyard.car.domain.Car;
import junkyard.member.domain.MemberUser;
import junkyard.reservation.domain.estimate.Estimate;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_id", nullable = false, unique = true)
    private String reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberUser memberUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private State status;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Estimate> estimates = new HashSet<>();

    @Builder
    public Reservation(String reservationId, MemberUser memberUser, Car car, String content) {
        this.reservationId = reservationId;
        this.memberUser = memberUser;
        this.car = car;
        this.status = State.PENDING;
        this.content = content;
    }

    public enum State {
        PENDING, CONFIRMED, CANCELED, REJECTED, COMPLETED, EXPIRED, ON_HOLD
    }
}
