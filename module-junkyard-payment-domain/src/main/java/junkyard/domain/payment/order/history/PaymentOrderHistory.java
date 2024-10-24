package junkyard.domain.payment.order.history;

import jakarta.persistence.*;
import junkyard.domain.payment.order.PaymentOrder;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "payment_order_histories")
public class PaymentOrderHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "payment_order_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private PaymentOrder paymentOrder;

    @Column(name = "previous_status")
    @Enumerated(EnumType.STRING)
    private PaymentOrder.PaymentOrderStatus previousStatus;

    @Column(name = "new_status")
    @Enumerated(EnumType.STRING)
    private PaymentOrder.PaymentOrderStatus new_status;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "reason")
    private String reason;

    @Builder
    public PaymentOrderHistory(PaymentOrder paymentOrder, PaymentOrder.PaymentOrderStatus previousStatus,
                               PaymentOrder.PaymentOrderStatus new_status, String changedBy, String reason) {
        this.paymentOrder = paymentOrder;
        this.previousStatus = previousStatus;
        this.new_status = new_status;
        this.createdAt = createdAt;
        this.changedBy = changedBy;
        this.reason = reason;
    }

    public void setPaymentOrder(PaymentOrder paymentOrder) {
        this.paymentOrder = paymentOrder;
    }
}
