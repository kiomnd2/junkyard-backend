package junkyard.domain.reservation;


import jakarta.persistence.*;
import junkyard.domain.BaseEntity;
import junkyard.domain.car.Car;
import junkyard.domain.member.MemberUser;
import junkyard.domain.reservation.estimate.Estimate;
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

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private MemberUser memberUser;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Estimate> estimates = new HashSet<>();

    @Builder
    public Reservation(MemberUser memberUser, Car car, LocalDateTime startTime, LocalDateTime endTime, String status,
                       String content, String cancellationReason, Set<Estimate> estimates) {
        this.memberUser = memberUser;
        this.car = car;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.content = content;
        this.cancellationReason = cancellationReason;
        this.estimates = estimates;
    }
}