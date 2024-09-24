package junkyard.reservation.domain.estimate;

import jakarta.persistence.*;
import junkyard.BaseEntity;
import junkyard.reservation.domain.Reservation;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "estimates")
public class Estimate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "issued_date", nullable = false)
    private LocalDateTime issuedDate;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_final", nullable = false)
    private Boolean isFinal;

    @Builder
    public Estimate(Reservation reservation, Double amount, LocalDateTime issuedDate, String description, Boolean isFinal) {
        this.reservation = reservation;
        this.amount = amount;
        this.issuedDate = issuedDate;
        this.description = description;
        this.isFinal = isFinal;
    }

    public EstimateInfo toInfo() {
        return EstimateInfo.builder()
                .amount(amount)
                .issuedDate(issuedDate)
                .isFinal(isFinal)
                .description(description)
                .build();
    }

    public void notFinal() {
        this.isFinal = false;
    }
}